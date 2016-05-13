This is the instruction manual for the FNF locator app. It was designed and developed by Richard Downs and S M Azharul Karim.

App structure:
1. Activities
	A. Login
	B. Main Menu
	C. Create Clock
	D. Display Clock
2. Service
	A. Refresh
3. Broadcast
	A. Boot
	B. Sensor Listener
4. Database
	A. 2 Local – SQLite
	B. Remote – Amazon Web Services
5. AWS Mobile Client and Configuration files

App operation:
1. The Username can only be set on Login
2. The user location is defined in the preferences and updated by interacting with the app
3. Manual vs Automatic operation
4. Create custom groups
5. Display the custom groups
6. In the background
7. Updating the remote database as soon as the user changes location
	>Pulling user location from the remote database at user set intervals

