package fanvu.easygoer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fanvu.easygoer.asynctask.GcmSenderAsyncTask;
import fanvu.easygoer.common.CheckConnect;
import fanvu.easygoer.common.RequestMethod;
import fanvu.easygoer.common.RestClient;
import fanvu.easygoer.config.Config;
import fanvu.easygoer.gcm.R;
import fanvu.easygoer.gcm.RegisterActivity;
//import fanvu.easygoer.common.GPSTracker;

public class LoginActivity extends Activity implements OnClickListener {
    private Button mSignInButton;
    private TextView mRegisterTextView;
    private EditText password;
    private EditText username;
    private Context _context;
    private String mTypeOfUser;
    Boolean isConnectionExist = false;
    CheckConnect _checkConnect;
    private String mUserName;
    private String mPassword;
    private ProgressDialog pDialog;
    //GPSTracker gps;
    //private double latitude;
    //private double longitude;
    private static String TAG = "LoginActivity";
    private String mWard0 = "";
    private String mWard1 = "";
    private String mWard2 = "";
    private SharedPreferences mPreferences;

    //	MobileWSServiceLocator locator = new MobileWSServiceLocator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = LoginActivity.this;
        _checkConnect = new CheckConnect(_context);
        pDialog = new ProgressDialog(_context);
        //gps = new GPSTracker(_context);
        mPreferences = getSharedPreferences("my_data", MODE_PRIVATE);
        boolean isLogin = mPreferences.getBoolean("isLogin", false);
        android.util.Log.d(TAG, "Logged in: " + isLogin);
        if (isLogin) {
            mUserName = mPreferences.getString("username", "");
            mPassword = mPreferences.getString("password", "");
            mTypeOfUser = mPreferences.getString("typeOfUser", "");
            doSignIn();
        }
        setContentView(R.layout.activity_login);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mRegisterTextView = (TextView) findViewById(R.id.register_textView);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        mSignInButton.setOnClickListener(this);
        mRegisterTextView.setOnClickListener(this);
    }

    private AlertDialog createDialogError(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                username.setText("");
                password.setText("");
                username.requestFocus();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                mUserName = username.getText().toString();
                mPassword = password.getText().toString();
                doSignIn();
                break;
            case R.id.register_textView:
                doRegister();
                break;
        }
    }

    /*
    private void getLocation() {
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            //MapCache.createdBy= mUserName;
            android.util.Log.d(TAG, "GPS: " + latitude + " / " + longitude);
            Geocoder geocoder = new Geocoder(_context, Locale.getDefault());
            Address address = null;
            String result = null;
            List<Address> list = null;
            try {
                list = geocoder.getFromLocation(latitude, longitude, 1);
                address = list.get(0);
                int maxAddressLine = address.getMaxAddressLineIndex();
                if (maxAddressLine >= 3) {
                    mWard2 = address.getAddressLine(maxAddressLine - 1);
                    mWard1 = address.getAddressLine(maxAddressLine - 2);
                    mWard0 = address.getAddressLine(maxAddressLine - 3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            android.util.Log.d(TAG, "Adress: " + result);
        }
    }

    */

    private void doSignIn() {
        android.util.Log.d(TAG, "SignIn");
        isConnectionExist = _checkConnect.checkData();
        //getLocation();
        if (mUserName.equals("") || mPassword.equals("")) {
            AlertDialog myErrorDialog = createDialogError("Bạn phải nhập user và pass");
            myErrorDialog.show();
        }
        if (mUserName.equals("anhpt") && mPassword.equals("anhpt")) {
            Toast.makeText(_context, "Đăng nhập thành công",
                Toast.LENGTH_SHORT).show();
            //goToAreanSelectorActivity();
        } else {
            if (isConnectionExist) {
                //mLog.info("Login end success");
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            } else {
                Log.e("anhpt67", "Login end fail");
                AlertDialog myErrorDialog = createDialogError("Vui lòng kiểm tra lại kết nối mạng");
                myErrorDialog.show();
            }
        }
    }

    private void doRegister() {
        android.util.Log.d(TAG, "Register");
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        private String Content;
        private String Error = null;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(_context);
            pDialog.setTitle("Login...");
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Send POST data request
                Content = getRespose(Config.URL_WS + "mobile/authen");
            } catch (Exception ex) {
                Error = ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pDialog.dismiss();
            if (Error != null) {
                Toast.makeText(_context, Error, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    if (Content.equals("") || Content == null
                        || Content.equals("-1") || Content.contains("error")) {
                        AlertDialog myErrorDialog =
                            createDialogError("Dang nhap khong thanh cong " + Content);
                        myErrorDialog.show();
                    } else {
                        ArrayList<TripInfo> tripInfoArrayList = new ArrayList<TripInfo>();
                        JSONObject jsonResponse;
                        jsonResponse = new JSONObject(Content);
                        if (jsonResponse.has("lstTrip")) {
                            JSONArray jsonArray = new JSONArray();
                            jsonArray = jsonResponse.getJSONArray("lstTrip");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTrip = null;
                                jsonTrip = jsonArray.getJSONObject(i);
                                if (jsonTrip != null) {
                                    TripInfo tripInfo;
                                    // TripInfo(String tripId, String tripPrice, String timeStart, String placeStart, String placeEnd, String nameDriver, String phone, String comment) {
                                    tripInfo = new TripInfo();
                                    tripInfo.setTripId(jsonTrip.getString("tripId"));
                                    tripInfo.setTripPrice(jsonTrip.getString("tripPrice"));
                                    tripInfo.setTimeStart(jsonTrip.getString("timeStart"));
                                    tripInfo.setPlaceStart(jsonTrip.getString("placeStart"));
                                    tripInfo.setPlaceEnd(jsonTrip.getString("placeEnd"));
                                    tripInfo.setNameDriver(jsonTrip.getString("nameDriver"));
                                    tripInfo.setPhone(jsonTrip.getString("phone"));
                                    tripInfo.setComment(jsonTrip.getString("comment"));
//                                    tripInfo = new TripInfo(jsonTrip.getString("tripId"), jsonTrip.getString("tripPrice"),
//                                            jsonTrip.getString("timeStart"), jsonTrip.getString("placeStart"),
//                                            jsonTrip.getString("placeEnd"), jsonTrip.getString("nameDriver"),
//                                            jsonTrip.getString("phone"), jsonTrip.getString("comment"));
                                    tripInfoArrayList.add(tripInfo);
                                }
                            }
                            mTypeOfUser = jsonResponse.getString("typeUser");
                            //save data when logged in
                            SharedPreferences.Editor edit = mPreferences.edit();
                            edit.putBoolean("isLogin", true);
                            edit.putString("username", mUserName);
                            edit.putString("password", mPassword);
                            edit.putString("typeOfUser", mTypeOfUser);
                            edit.commit();
                            //Start activity listTrip
                            Intent intent = new Intent(LoginActivity.this, ListTripActivity.class);
                            intent.putExtra("ListTrip", new TripInfoArray(tripInfoArrayList));
                            intent.putExtra("UserName", mUserName);
                            intent.putExtra("Password", mPassword);
                            intent.putExtra("TypeOfUser", mTypeOfUser);
                            startActivity(intent);
                            finish();
                        } else {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        android.util.Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        android.util.Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        android.util.Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        android.util.Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        android.util.Log.d(TAG, "onStop");
        super.onStop();
    }

    public String getRespose(String url) {
        String response = "";
        try {
            RestClient client = new RestClient(url);
            client.AddHeader("username", mUserName);
            client.AddHeader("password", mPassword);
            client.AddHeader("wardid0", mWard0);
            client.AddHeader("wardid1", mWard1);
            client.AddHeader("wardid2", mWard2);
//            client.AddParam("username", mUserName);
//            client.AddParam("password", mPassword);
//            client.AddParam("wardid0", mWard0);
//            client.AddParam("wardid1", mWard1);
//            client.AddParam("wardid2", mWard2);
            android.util.Log.d(TAG,
                "getResponse: " + "Username: " + mUserName + " / password: " + mPassword +
                    " / wardID0: " + mWard0 + " / Wardid1: " + mWard1 + " / wardID2: " + mWard2);
            try {
                client.Execute(RequestMethod.POST);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response = client.getResponse();
            android.util.Log.d(TAG, "reponse: " + response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }
}
