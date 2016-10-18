package fanvu.easygoer.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fanvu.easygoer.Constant;
import fanvu.easygoer.common.CheckConnect;
import fanvu.easygoer.common.Utils;
import fanvu.easygoer.gcm.R;
import fanvu.easygoer.gcm.RegisterActivity;
import fanvu.easygoer.myinterface.ApiInterface;
import fanvu.easygoer.object.ApiClient;
import fanvu.easygoer.object.ListTripResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import fanvu.easygoer.common.GPSTracker;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
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
        setContentView(R.layout.activity_login);
        Utils.addView();
        _context = LoginActivity.this;
        _checkConnect = new CheckConnect(_context);
        pDialog = new ProgressDialog(_context);
        pDialog.setTitle("Login...");
        //gps = new GPSTracker(_context);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mRegisterTextView = (TextView) findViewById(R.id.register_textView);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        mSignInButton.setOnClickListener(this);
        mRegisterTextView.setOnClickListener(this);
        mPreferences = getSharedPreferences(Constant.SHARE_PREFERENCE, Context.MODE_PRIVATE);
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
        isConnectionExist = _checkConnect.checkData();
        if (mUserName.equals("") || mPassword.equals("")) {
            AlertDialog myErrorDialog = createDialogError("Bạn phải nhập user và pass");
            myErrorDialog.show();
        }
        if (mUserName.equals("anhpt") && mPassword.equals("anhpt")) {
            Toast.makeText(_context, "Đăng nhập thành công",
                Toast.LENGTH_SHORT).show();
        } else {
            if (isConnectionExist) {
                logIn();
            } else {
                AlertDialog myErrorDialog = createDialogError("Vui lòng kiểm tra lại kết nối mạng");
                myErrorDialog.show();
            }
        }
    }

    private void logIn() {
        pDialog.show();
        ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);
        Call<ListTripResponse> call = apiService.getListTrip(mUserName
            , mPassword
            , ""
            , ""
            , "");
        call.enqueue(new Callback<ListTripResponse>() {
            @Override
            public void onResponse(Call<ListTripResponse> call,
                                   Response<ListTripResponse> response) {
                pDialog.dismiss();
                mTypeOfUser = response.body().getTypeUser();
                SharedPreferences.Editor edit = mPreferences.edit();
                edit.putBoolean(Constant.SHARE_IS_LOGIN, true);
                edit.putString(Constant.SHARE_USER_PHONE, mUserName);
                edit.putString(Constant.SHARE_PASSWORD, mPassword);
                edit.putString(Constant.SHARE_TYPE_USER, mTypeOfUser);
                edit.commit();
                Intent intent = new Intent(LoginActivity.this, ListTripActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ListTripResponse> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(_context, R.string.string_login_fail,
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doRegister() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
