package fanvu.easygoer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import fanvu.easygoer.common.NotificationUtils;
import fanvu.easygoer.common.Utils;
import fanvu.easygoer.gcm.Config;
import fanvu.easygoer.gcm.R;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String TAG = "GCMIntentService";
    private Controller aController = null;

    public GCMIntentService() {
        // Call extended class Constructor GCMBaseIntentService
        super(Config.GOOGLE_SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        if (aController == null)
            aController = (Controller) getApplicationContext();
        Log.i(TAG, "Device registered: regId = " + registrationId);
        aController.displayMessageOnScreen(context, "Your device registred with GCM");
        Log.d("NAME", MainActivity.name);
        aController.register(context, MainActivity.name, MainActivity.email, MainActivity.mobile,
            MainActivity.password, registrationId);
    }

    /**
     * Method called on device unregistred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        if (aController == null)
            aController = (Controller) getApplicationContext();
        Log.i(TAG, "Device unregistered");
        aController.displayMessageOnScreen(context, getString(R.string.gcm_unregistered));
        aController.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message from GCM server
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        String message = intent.getExtras().getString("message");
        Utils.displayMessageOnScreen(context, message);
        NotificationUtils.pushNotification(context, "new message", message);
        Utils.addView(context);
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        if (aController == null)
            aController = (Controller) getApplicationContext();
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        aController.displayMessageOnScreen(context, message);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        if (aController == null)
            aController = (Controller) getApplicationContext();
        Log.i(TAG, "Received error: " + errorId);
        aController.displayMessageOnScreen(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        if (aController == null)
            aController = (Controller) getApplicationContext();
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        aController.displayMessageOnScreen(context, getString(R.string.gcm_recoverable_error,
            errorId));
        return super.onRecoverableError(context, errorId);
    }
}
