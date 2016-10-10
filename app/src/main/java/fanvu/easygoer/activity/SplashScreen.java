package fanvu.easygoer.activity;

import android.app.Activity;
import android.os.Bundle;

import fanvu.easygoer.asynctask.CheckLoginAsyncTask;
import fanvu.easygoer.gcm.R;

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        CheckLoginAsyncTask checkLoginAsyncTask = new CheckLoginAsyncTask();
        checkLoginAsyncTask.execute(this);
    }
}
