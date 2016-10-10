package fanvu.easygoer.asynctask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import fanvu.easygoer.Constant;
import fanvu.easygoer.ListTripActivity;
import fanvu.easygoer.LoginActivity;

/**
 * Created by framgia on 09/10/2016.
 */
public class CheckLoginAsyncTask extends AsyncTask<Context, Void, Void> {
    boolean isLogin;
    Context mContext;

    @Override
    protected Void doInBackground(Context... params) {
        mContext = params[0];
        SharedPreferences mPreferences = mContext.getSharedPreferences(Constant.SHARE_PREFERENCE,
            Context.MODE_PRIVATE);
        isLogin = mPreferences.getBoolean("isLogin", false);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (isLogin) {
            Intent intent = new Intent(mContext, ListTripActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }
    }
}
