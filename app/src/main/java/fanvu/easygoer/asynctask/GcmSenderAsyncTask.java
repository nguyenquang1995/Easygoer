package fanvu.easygoer.asynctask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import fanvu.easygoer.gcm.GcmSender;

/**
 * Created by framgia on 05/10/2016.
 */
public class GcmSenderAsyncTask extends AsyncTask<Void, Void, Void> {
    String[] args = {"PhanVV_Test_Hello", GcmSender.quang_reg_id};
    private List<String> mStringList = new ArrayList<>();
    @Override
    protected Void doInBackground(Void... params) {
        mStringList.add(GcmSender.quang_reg_id);
        GcmSender.main(args);
        //GcmSender.sendToMobiles("PhanVV_Test_Hello", mStringList);
        return null;
    }
}
