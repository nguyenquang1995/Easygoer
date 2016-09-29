package fanvu.easygoer.gcm;

public interface Config {

	
	// CONSTANTS
	static final String YOUR_SERVER_URL =  "https://easygo-fanvu.rhcloud.com/rest/mobile/registerUserAndGcm";
	// YOUR_SERVER_URL : Server url where you have placed your server files
    // Google project id
    static final String GOOGLE_SENDER_ID = "405574473178";  // Place here your Google project id

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";

    static final String DISPLAY_MESSAGE_ACTION ="fanvu.easygoer.gcm.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";


    static final String YOUR_SERVER_URL_POST_TRIP =  "https://easygo-fanvu.rhcloud.com/rest/mobile/postTripUserAndNotify";
		
	
}
