package fanvu.easygoer.asynctask;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;

import fanvu.easygoer.gcm.GcmSender;

/**
 * Created by framgia on 05/10/2016.
 */
public class GcmSenderAsyncTask extends AsyncTask<Void, Void, Void> {
    private String message;
    private String registerId;

    public GcmSenderAsyncTask(String registerId, String message) {
        this.registerId = registerId;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            GcmSender.sendMessageToClient(registerId, message);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
