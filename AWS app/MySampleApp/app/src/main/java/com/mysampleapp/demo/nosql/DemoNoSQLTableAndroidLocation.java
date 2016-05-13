package com.mysampleapp.demo.nosql;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.util.ThreadUtils;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.mysampleapp.AndroidLocationDO;
import com.mysampleapp.R;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class DemoNoSQLTableAndroidLocation extends DemoNoSQLTableBase {
    private static final String LOG_TAG = DemoNoSQLTableAndroidLocation.class.getSimpleName();

    /** Condition demo key for Primary Queries with filter conditions. */
    private static final String DEMO_PRIMARY_CONDITION_KEY = "password";
    /** Condition demo value for Queries with filter conditions. */
    private static final String DEMO_PRIMARY_CONDITION_VALUE = "demo-" + DEMO_PRIMARY_CONDITION_KEY + "-500000";

    /** Condition demo key for Secondary Queries with filter conditions. */
    private static final String DEMO_SECONDARY_CONDITION_KEY = "password";
    /** Condition demo value for Queries with filter conditions. */
    private static final String DEMO_SECONDARY_CONDITION_VALUE = "demo-" + DEMO_SECONDARY_CONDITION_KEY + "-500000";

    private static final String DEMO_PARTITION_KEY = "userName";
  /** The Primary Partition Key Name.  All queries must use an equality condition on this key. */
    private static final String DEMO_PARTITION_VALUE = "Azharul";
    private String getDemoPartitionValue() {
        return DEMO_PARTITION_VALUE;
    }
    private String getDemoPartitionValueText() {
        return DEMO_PARTITION_VALUE;
    }
    private static final String DEMO_PARTITION_VALUE_TEXT = DEMO_PARTITION_VALUE;

    /********* Primary Get Query Inner Classes *********/

    public class DemoGetWithPartitionKey extends DemoNoSQLOperationBase {
        private AndroidLocationDO result;
        private boolean resultRetrieved = true;

        private DemoGetWithPartitionKey(final Context context) {
            super(context.getString(R.string.nosql_operation_get_by_partition_text),
                String.format(context.getString(R.string.nosql_operation_example_get_by_partition_text),
                    DEMO_PARTITION_KEY, getDemoPartitionValueText()));
        }

        /* Blocks until result is retrieved, should be called in the background. */
        @Override
        public boolean executeOperation() throws AmazonClientException {
            // Retrieve an item by passing the partition key using the object mapper.
            result = mapper.load(AndroidLocationDO.class, getDemoPartitionValue());

            if (result != null) {
                resultRetrieved = false;
                return true;
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            if (resultRetrieved) {
                return null;
            }
            final List<DemoNoSQLResult> results = new ArrayList<>();
            results.add(new DemoNoSQLAndroidLocationResult(result));
            resultRetrieved = true;
            return results;
        }

        @Override
        public void resetResults() {
            resultRetrieved = false;
        }

    }

    /* ******** Secondary Named Index Query Inner Classes ******** */

    /********* Scan Inner Classes *********/

    public class DemoScanWithFilter extends DemoNoSQLOperationBase {

        private PaginatedScanList<AndroidLocationDO> results;
        private Iterator<AndroidLocationDO> resultsIterator;

        DemoScanWithFilter(final Context context) {
            super(context.getString(R.string.nosql_operation_title_scan_with_filter),
                String.format(context.getString(R.string.nosql_operation_example_scan_with_filter),
                    DEMO_PRIMARY_CONDITION_KEY, DEMO_PRIMARY_CONDITION_VALUE));
        }

        @Override
        public boolean executeOperation() {
            // Use an expression names Map to avoid the potential for attribute names
            // colliding with DynamoDB reserved words.
            final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
            filterExpressionAttributeNames.put("#password", DEMO_PRIMARY_CONDITION_KEY);

            final Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
            filterExpressionAttributeValues.put(":Minpassword",
                new AttributeValue().withS(DEMO_PRIMARY_CONDITION_VALUE));
            final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("#password > :Minpassword")
                .withExpressionAttributeNames(filterExpressionAttributeNames)
                .withExpressionAttributeValues(filterExpressionAttributeValues);

            results = mapper.scan(AndroidLocationDO.class, scanExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public boolean isScan() {
            return true;
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    public class DemoScanWithoutFilter extends DemoNoSQLOperationBase {

        private PaginatedScanList<AndroidLocationDO> results;
        private Iterator<AndroidLocationDO> resultsIterator;

        DemoScanWithoutFilter(final Context context) {
            super(context.getString(R.string.nosql_operation_title_scan_without_filter),
                context.getString(R.string.nosql_operation_example_scan_without_filter));
        }

        @Override
        public boolean executeOperation() {
            final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            results = mapper.scan(AndroidLocationDO.class, scanExpression);
            if (results != null) {
                resultsIterator = results.iterator();
                if (resultsIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<DemoNoSQLResult> getNextResultGroup() {
            return getNextResultsGroupFromIterator(resultsIterator);
        }

        @Override
        public boolean isScan() {
            return true;
        }

        @Override
        public void resetResults() {
            resultsIterator = results.iterator();
        }
    }

    /**
     * Helper Method to handle retrieving the next group of query results.
     * @param resultsIterator the iterator for all the results (makes a new service call for each result group).
     * @return the next list of results.
     */
    private static List<DemoNoSQLResult> getNextResultsGroupFromIterator(final Iterator<AndroidLocationDO> resultsIterator) {
        if (!resultsIterator.hasNext()) {
            return null;
        }
        List<DemoNoSQLResult> resultGroup = new LinkedList<>();
        int itemsRetrieved = 0;
        do {
            // Retrieve the item from the paginated results.
            final AndroidLocationDO item = resultsIterator.next();
            // Add the item to a group of results that will be displayed later.
            resultGroup.add(new DemoNoSQLAndroidLocationResult(item));
            itemsRetrieved++;
        } while ((itemsRetrieved < RESULTS_PER_RESULT_GROUP) && resultsIterator.hasNext());
        return resultGroup;
    }

    /** Inner classes use this value to determine how many results to retrieve per service call. */
    private final static int RESULTS_PER_RESULT_GROUP = 40;
    /** Removing sample data removes the items in batches of the following size. */
    private static final int MAX_BATCH_SIZE_FOR_DELETE = 50;

    /** The table name. */
    private static final String TABLE_NAME = "AndroidLocation";
    /** The Primary Partition Key Type. */
    private static final String DEMO_PARTITION_KEY_TYPE = "String";
    /** The Sort Key Type. */
    private static final String DEMO_SORT_KEY_TYPE = "";
    /** The number of secondary table indexes. */
    private static final int NUM_TABLE_INDEXES = 0;

    /** The DynamoDB object mapper for accessing DynamoDB. */
    private final DynamoDBMapper mapper;


    public DemoNoSQLTableAndroidLocation() {
        mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getPartitionKeyName() {
        return DEMO_PARTITION_KEY;
    }

    public String getPartitionKeyType() {
        return DEMO_PARTITION_KEY_TYPE;
    }

    @Override
    public String getSortKeyName() {
        return null;
    }

    public String getSortKeyType() {
        return DEMO_SORT_KEY_TYPE;
    }

    @Override
    public int getNumIndexes() {
        return NUM_TABLE_INDEXES;
    }


    @Override
    public void insertSampleData() throws AmazonClientException {
        Log.d(LOG_TAG, "Inserting Sample data.");
        final AndroidLocationDO firstItem = new AndroidLocationDO();

        firstItem.setUserName(
            DemoSampleDataGenerator.getRandomSampleString("userName"));
        firstItem.setPassword(
            DemoSampleDataGenerator.getRandomSampleString("password"));
        firstItem.setStatus(
            DemoSampleDataGenerator.getRandomSampleString("status"));
        firstItem.setUserId(
            DemoSampleDataGenerator.getRandomSampleString("userId"));
        AmazonClientException lastException = null;

        try {
            mapper.save(firstItem);
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
            lastException = ex;
        }

        final AndroidLocationDO[] items = new AndroidLocationDO[SAMPLE_DATA_ENTRIES_PER_INSERT-1];
        for (int count = 0; count < SAMPLE_DATA_ENTRIES_PER_INSERT-1; count++) {
            final AndroidLocationDO item = new AndroidLocationDO();
            item.setUserName(DemoSampleDataGenerator.getRandomSampleString("userName"));
            item.setPassword(DemoSampleDataGenerator.getRandomSampleString("password"));
            item.setStatus(DemoSampleDataGenerator.getRandomSampleString("status"));
            item.setUserId(DemoSampleDataGenerator.getRandomSampleString("userId"));

            items[count] = item;
        }
        try {
            mapper.batchSave(Arrays.asList(items));
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed saving item batch : " + ex.getMessage(), ex);
            lastException = ex;
        }

        if (lastException != null) {
            // Re-throw the last exception encountered to alert the user.
            throw lastException;
        }
    }

    @Override
    public void removeSampleData() throws AmazonClientException {
        // Scan for the sample data to remove it.

        // Use an expression names Map to avoid the potential for attribute names
        // colliding with DynamoDB reserved words.
        final Map <String, String> filterExpressionAttributeNames = new HashMap<>();
        filterExpressionAttributeNames.put("#hashAttribute","userName");

        final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":prefixVal", new AttributeValue().withS(
            DemoSampleDataGenerator.SAMPLE_DATA_STRING_PREFIX));

        final String hashKeyFilterCondition = "begins_with(#hashAttribute, :prefixVal)";

        final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression(hashKeyFilterCondition)
            .withExpressionAttributeNames(filterExpressionAttributeNames)
            .withExpressionAttributeValues(expressionAttributeValues);

        PaginatedScanList<AndroidLocationDO> results = mapper.scan(AndroidLocationDO.class, scanExpression);

        Iterator<AndroidLocationDO> resultsIterator = results.iterator();

        AmazonClientException lastException = null;

        if (resultsIterator.hasNext()) {
            final AndroidLocationDO item = resultsIterator.next();

            // Demonstrate deleting a single item.
            try {
                mapper.delete(item);
            } catch (final AmazonClientException ex) {
                Log.e(LOG_TAG, "Failed deleting item : " + ex.getMessage(), ex);
                lastException = ex;
            }
        }

        final List<AndroidLocationDO> batchOfItems = new LinkedList<AndroidLocationDO>();
        while (resultsIterator.hasNext()) {
            // Build a batch of books to delete.
            for (int index = 0; index < MAX_BATCH_SIZE_FOR_DELETE && resultsIterator.hasNext(); index++) {
                batchOfItems.add(resultsIterator.next());
            }
            try {
                // Delete a batch of items.
                mapper.batchDelete(batchOfItems);
            } catch (final AmazonClientException ex) {
                Log.e(LOG_TAG, "Failed deleting item batch : " + ex.getMessage(), ex);
                lastException = ex;
            }

            // clear the list for re-use.
            batchOfItems.clear();
        }


        if (lastException != null) {
            // Re-throw the last exception encountered to alert the user.
            // The logs contain all the exceptions that occurred during attempted delete.
            throw lastException;
        }
    }

    private List<DemoNoSQLOperationListItem> getSupportedDemoOperations(final Context context) {
        List<DemoNoSQLOperationListItem> noSQLOperationsList = new ArrayList<DemoNoSQLOperationListItem>();
            noSQLOperationsList.add(new DemoGetWithPartitionKey(context));

        noSQLOperationsList.add(new DemoNoSQLOperationListHeader(
            context.getString(R.string.nosql_operation_header_scan)));
        noSQLOperationsList.add(new DemoScanWithoutFilter(context));
        noSQLOperationsList.add(new DemoScanWithFilter(context));
        return noSQLOperationsList;
    }

    @Override
    public void getSupportedDemoOperations(final Context context,
                                           final SupportedDemoOperationsHandler opsHandler) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                opsHandler.onSupportedOperationsReceived(getSupportedDemoOperations(context));
            }
        });
    }
}
