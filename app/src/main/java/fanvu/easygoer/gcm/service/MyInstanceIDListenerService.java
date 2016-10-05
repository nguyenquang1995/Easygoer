package fanvu.easygoer.gcm.service;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by framgia on 04/10/2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
