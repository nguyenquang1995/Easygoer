package fanvu.easygoer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fanvu.easygoer.Constant;
import fanvu.easygoer.common.CheckConnect;
import fanvu.easygoer.common.GPSTracker;
import fanvu.easygoer.common.RequestMethod;
import fanvu.easygoer.common.RestClient;
import fanvu.easygoer.gcm.R;

/**
 * Created by framgia on 10/10/2016.
 */
public class PostripFragment extends Fragment implements View.OnClickListener {
    private Button mPostTripButton;
    private EditText mWardStartEdt;
    private EditText mDistrictStartEdt;
    private EditText mProvinceStartEdt;
    private EditText mWardEndEdt;
    private EditText mDistrictEndEdt;
    private EditText mProvinceEndEdt;
    private TextView mTimeStartTxt;
    private EditText mPriceEdt;
    private EditText mCommentEdt;
    private Context mContext;
    private Button mBtnDatePicker;
    private Dialog mTimePickerDialog;
    Boolean isConnectionExist = false;
    CheckConnect _checkConnect;
    private String mUserName;
    private String mPassword;
    private String mPlaceStart;
    private String mPlaceEnd;
    private String mWardNameStart;
    private String mDistrictNameStart;
    private String mProvinceNameStart;
    private String mWardNameEnd;
    private String mDistrictNameEnd;
    private String mProvinceNameEnd;
    private String mTimeStart;
    private String mPrice;
    private String mComment;
    private String mTypeOfUser;
    private ProgressDialog pDialog;
    GPSTracker gps;
    String serverUrl = fanvu.easygoer.gcm.Config.YOUR_SERVER_URL_POST_TRIP;
    Map<String, String> paramsIn = new HashMap<String, String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_trip, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant
                .SHARE_PREFERENCE
            , Context.MODE_PRIVATE);
        mUserName = sharedPreferences.getString("username", "");
        mPassword = sharedPreferences.getString("password", "");
        mTypeOfUser = sharedPreferences.getString("typeOfUser", "");
        mContext = getActivity();
        _checkConnect = new CheckConnect(mContext);
        mPostTripButton = (Button) view.findViewById(R.id.post_trip_button);
        mWardStartEdt = (EditText) view.findViewById(R.id.wardStart);
        mDistrictStartEdt = (EditText) view.findViewById(R.id.dictricStart);
        mProvinceStartEdt = (EditText) view.findViewById(R.id.provinceStart);
        mWardEndEdt = (EditText) view.findViewById(R.id.wardEnd);
        mDistrictEndEdt = (EditText) view.findViewById(R.id.dictricEnd);
        mProvinceEndEdt = (EditText) view.findViewById(R.id.provinceEnd);
        mTimeStartTxt = (TextView) view.findViewById(R.id.timeStart);
        mPriceEdt = (EditText) view.findViewById(R.id.price);
        mCommentEdt = (EditText) view.findViewById(R.id.comment);
        mBtnDatePicker = (Button) view.findViewById(R.id.buttonDatePicker);
        pDialog = new ProgressDialog(mContext);
        mTimePickerDialog = createTimePickerDialog();
        gps = new GPSTracker(mContext);
        setCurrentTimePicker();
        mPostTripButton.setOnClickListener(this);
        mBtnDatePicker.setOnClickListener(this);
    }

    private void setCurrentTimePicker() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        mTimeStart = df.format(Calendar.getInstance().getTime());
        mTimeStartTxt.setText(mTimeStart);
    }

    private Dialog createTimePickerDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.set_time_dialog_layout);
        dialog.setTitle("Cài đặt thời gian");
        Button doneBtn = (Button) dialog.findViewById(R.id.doneButton);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String month = "" + (datePicker.getMonth() < 10 ? "0" + datePicker.getMonth() :
                    datePicker.getMonth());
                String day =
                    "" + (datePicker.getDayOfMonth() < 10 ? "0" + datePicker.getDayOfMonth() :
                        datePicker.getDayOfMonth());
                String hour =
                    "" + (timePicker.getCurrentHour() < 10 ? "0" + timePicker.getCurrentHour() :
                        timePicker.getCurrentHour());
                String minute =
                    "" + (timePicker.getCurrentMinute() < 10 ? "0" + timePicker.getCurrentMinute() :
                        timePicker.getCurrentMinute());
                mTimeStart =
                    "" + datePicker.getYear() + "/" + month + "/" + day + " " + hour + ":" +
                        minute + ":00";
                mTimeStartTxt.setText(mTimeStart);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    private AlertDialog createDialog(String msg, final boolean isSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông báo");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isSuccess) {
                } else {
                }
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_trip_button:
                doPostTrip();
                break;
            case R.id.buttonDatePicker:
                mTimePickerDialog.show();
                break;
        }
    }

    private void doPostTrip() {
        mWardNameStart = mWardStartEdt.getText().toString();
        mDistrictNameStart = mDistrictStartEdt.getText().toString();
        mProvinceNameStart = mProvinceStartEdt.getText().toString();
        mWardNameEnd = mWardEndEdt.getText().toString();
        mDistrictNameEnd = mDistrictEndEdt.getText().toString();
        mProvinceNameEnd = mProvinceEndEdt.getText().toString();
        mTimeStart = mTimeStartTxt.getText().toString();
        mPrice = mPriceEdt.getText().toString();
        mComment = mCommentEdt.getText().toString();
        mPlaceStart = mWardNameStart + "-" + mDistrictNameStart + "-" + mProvinceNameStart;
        mPlaceEnd = mWardNameEnd + "-" + mDistrictNameEnd + "-" + mProvinceNameEnd;
        paramsIn.put("placeStart", mPlaceStart);
        paramsIn.put("placeEnd", mPlaceEnd);
        paramsIn.put("timeStart", mTimeStart);
        paramsIn.put("tripPrice", mPrice);
        if (mTypeOfUser.contains("driver")) {
            paramsIn.put("tripType", "1");
        } else {
            paramsIn.put("tripType", "3");
        }
        paramsIn.put("username", mUserName);
        paramsIn.put("password", mPassword);
        paramsIn.put("comment", mComment);
        isConnectionExist = _checkConnect.checkData();
        if (isConnectionExist) {
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        } else {
            AlertDialog myErrorDialog = createDialog("Vui lòng kiểm tra lại kết nối mạng", false);
            myErrorDialog.show();
        }
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        private String Content;
        private String Error = null;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(mContext);
            pDialog.setTitle("Post Trip...");
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Content = post(serverUrl, paramsIn);
            } catch (Exception ex) {
                Error = ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pDialog.dismiss();
            if (Error != null) {
                Toast.makeText(mContext, Error, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    if (Content != null && Content.contains("post_trip_ok")) {
                        AlertDialog myDialog = createDialog("Post Trip thanh cong", true);
                        myDialog.show();
                    } else {
                        AlertDialog myDialog = createDialog("Post Trip khong thành công", false);
                        myDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRespose(String url) {
        String response = "";
        try {
            RestClient client = new RestClient(url);
            client.AddHeader("placeStart", mPlaceStart);
            client.AddHeader("placeEnd", mPlaceEnd);
            client.AddHeader("timeStart", mTimeStart);
            client.AddHeader("tripPrice", mPrice);
            client.AddHeader("numberCustomer", "2");
            if (mTypeOfUser.contains("driver")) {
                client.AddHeader("tripType", "1");
            } else {
                client.AddHeader("tripType", "3");
            }
            client.AddHeader("username", mUserName);
            client.AddHeader("password", mPassword);
            client.AddHeader("wardNameStart", mWardNameStart);
            client.AddHeader("districtNameStart", mDistrictNameStart);
            client.AddHeader("provinceNameStart", mProvinceNameStart);
            client.AddHeader("wardNameEnd", mWardNameEnd);
            client.AddHeader("districtNameEnd", mDistrictNameEnd);
            client.AddHeader("provinceNameEnd", mProvinceNameEnd);
            client.AddHeader("comment", mComment);
            try {
                client.Execute(RequestMethod.POST);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response = client.getResponse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

    // Added 4/9/2016 by PhanVV
    // Using for font Vietnamese character.
    private static String post(String endpoint, Map<String, String> params)
        throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(fanvu.easygoer.gcm.Config.TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            // If response is not success
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return "post_trip_ok";
    }
}
