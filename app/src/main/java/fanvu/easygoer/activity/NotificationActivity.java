package fanvu.easygoer.activity;

import android.app.Activity;
import android.os.Bundle;

import fanvu.easygoer.common.Utils;
import fanvu.easygoer.gcm.R;

public class NotificationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Utils.sBitmap.recycle();
    }
}
