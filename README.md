# gcmChatClient
gcmChatClient is an Android chat client that relies on Google Cloud Messaging for push from the chat server to the client. Messages are received from a sticky IntentService to receive messages from Google Cloud Messaging. Http is used to send chat
messages to a web service.  An companion webservice and Google Cloud Message 3rd party server can be found at https://github.com/mrblasto/gcm_ChatServer_nodejs

This project and the companion server were written as a learning exercise and proof of concept prototype for another project.
As with most code, it is based on the work of others. To persist messages and contacts a local SQLite database is used. The DataProvider accessing the database is based on code found at http://www.appsrox.com/android/tutorials/instachat/2. The webservice client code is based on the post functions in the ServerUtilities.java class found at from the same tutorial which itself is based on code from https://github.com/pepalo/peep/wiki/GCM-and-Server-Utilities.  Most of the rest of the code is based on the tutorials and examples at https://developer.android.com.  Specifically the Google Cloud Messaging client code is based on the example at https://developer.android.com/google/gcm/index.html.

## Getting Started Using the gcmChatClient
The client requires both the Google Play and Google Cloud Messaging APIs.  To use these, you'll need to create a Google API project and obtain a project number and Google API access key (for the chat server).  Details can be found at https://developer.android.com/google/gcm/gs.html.  The device or emulator you run the client on will need to have the Google Play Services app installed.  Fortunately most devices come with this pre-installed.

Although this project was developed using Intellij, it should be simple to compile it with other IDEs.  Dependencies include the appcompat library from at least version 22.1.1 of the Android Support Library as well at least version 24 of the Google Play services library.  In order to make sure you can get all the resources these libraries should be included as library modules in your project.  In turn both the library modules and the jar files should be listed as dependencies for the gcmChatClient.  Both the v4 and v7 jars of the appcompat support library should be included in the gcmChatClient dependencies.

You'll need to modify two lines of code to enter installation dependent information.  At line 12 of Common.java change the existing url to the url of your chat server:

    // change this to your web service's endpoint
    private static String baseUrl = "http://10.0.0.4:3000";

At line 46 of MainActivity.java change the project id to the project id of the project set up in your Google API project:

    // fill in your Google Project Number (available from https://developers.google.com/)
    private String PROJECT_NUMBER = "123456789fake";
    
## Using the gcmChatClient

You must register a Google Play account in order to use the chat client.  If no account has been selected a dialog will pop up asking you to select a Google Play account.  The emails of existing accounts will be listed as well as an option to create a new account.

![Account Registration](/images/AccountScreen.png)

After entering an account and username press the contacts button in the right of the toolbar (square person icon).  This will navigate to the contacts page as well as obtain a Google Cloud Messaging token for the client and registering the token, username, and email with chat server.  If this is the first time the app has been used then a blank contacts window will be shown:

![Contacts Screen](/images/contactsScreen.png)

Note the three icons on the right of the action bar.  They have the following actions

![Toolbar contols](/images/Controls.png)

_Rest is under construction._
    
