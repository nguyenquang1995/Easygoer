package fanvu.easygoer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fanvu.easygoer.adapter.AdapterListTrip;
import fanvu.easygoer.asynctask.GcmSenderAsyncTask;
import fanvu.easygoer.common.CheckConnect;
import fanvu.easygoer.common.RequestMethod;
import fanvu.easygoer.common.RestClient;
import fanvu.easygoer.config.Config;
import fanvu.easygoer.gcm.R;
import fanvu.easygoer.mylistener.ItemTripClickListener;

public class ListTripActivity extends AppCompatActivity implements ItemTripClickListener {
    ArrayList<TripInfo> LoginTripInfoArrayList = new ArrayList();
    ArrayList<TripInfo> SearchTripInfoArrayList = new ArrayList();
    private RecyclerView mRecyclerView;
    private AdapterListTrip mAdapterListTrip;
    private String mUserName;
    private String mPassword;
    private String mTypeOfUser;
    private SearchView mSearchView;
    private ProgressDialog pDialog;
    Boolean isConnectionExist = false;
    CheckConnect _checkConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trip);
        init();
    }

    private void init() {
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GcmSenderAsyncTask asyncTask = new GcmSenderAsyncTask();
                asyncTask.execute();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mSearchView = (SearchView) findViewById(R.id.searchview);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Searching...");
        _checkConnect = new CheckConnect(this);
        Intent intent = getIntent();
        if (intent != null) {
            final TripInfoArray tripInfoArray =
                (TripInfoArray) intent.getSerializableExtra("ListTrip");
            mUserName = intent.getStringExtra("UserName");
            mPassword = intent.getStringExtra("Password");
            mTypeOfUser = intent.getStringExtra("TypeOfUser");
            LoginTripInfoArrayList = tripInfoArray.getTripInfoArray();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mAdapterListTrip = new AdapterListTrip(getApplicationContext(), LoginTripInfoArrayList);
            mAdapterListTrip.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mAdapterListTrip);
        }
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearching();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Dialog dialog = createDialog(LoginTripInfoArrayList.get(position).getPhone());
        dialog.show();
    }

    private AlertDialog createDialog(final String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Gọi điện thoại: ");
        builder.setMessage(msg);
        builder.setPositiveButton("Gọi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String number = "tel:" + msg;
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    startActivity(callIntent);
                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    final String timeYYYYMMDDhhmmss = df.format(Calendar.getInstance().getTime());
                    Thread callLog = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveLogCallToServer(mUserName, msg, timeYYYYMMDDhhmmss);
                        }
                    });
                    callLog.start();
                } catch (SecurityException e) {
                }
            }
        });
        builder.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_post_trip) {
            Intent intent = new Intent(ListTripActivity.this, PostTripActivity.class);
            intent.putExtra("UserName", mUserName);
            intent.putExtra("Password", mPassword);
            intent.putExtra("TypeOfUser", mTypeOfUser);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_log_out) {
            SharedPreferences mPreferences = getSharedPreferences("my_data", MODE_PRIVATE);
            SharedPreferences.Editor edit = mPreferences.edit();
            edit.putBoolean("isLogin", false);
            edit.commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doSearching() {
        AsyncCallWS task = new AsyncCallWS();
        task.execute();
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        private String Content;
        private String Error = null;

        @Override
        protected void onPreExecute() {
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
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
                Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    if (Content.equals("") || Content == null || Content.equals("-1") ||
                        Content.contains("error")) {
                        AlertDialog myErrorDialog =
                            createDialogError("Tim kiem khong thanh cong " + Content);
                        myErrorDialog.show();
                    } else {
                        JSONObject jsonResponse = new JSONObject(Content);
                        SearchTripInfoArrayList.clear();
                        if (jsonResponse.has("lstTrip")) {
                            JSONArray jsonArray = new JSONArray();
                            jsonArray = jsonResponse.getJSONArray("lstTrip");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTrip = null;
                                jsonTrip = jsonArray.getJSONObject(i);
                                if (jsonTrip != null) {
                                    TripInfo tripInfo = new TripInfo();
                                    tripInfo.setTripId(jsonTrip.getString("tripId"));
                                    tripInfo.setTripPrice(jsonTrip.getString("tripPrice"));
                                    tripInfo.setTimeStart(jsonTrip.getString("timeStart"));
                                    tripInfo.setPlaceStart(jsonTrip.getString("placeStart"));
                                    tripInfo.setPlaceEnd(jsonTrip.getString("placeEnd"));
                                    tripInfo.setNameDriver(jsonTrip.getString("nameDriver"));
                                    tripInfo.setPhone(jsonTrip.getString("phone"));
                                    tripInfo.setComment(jsonTrip.getString("comment"));
                                    SearchTripInfoArrayList.add(tripInfo);
                                }
                            }
                            mAdapterListTrip.mList = SearchTripInfoArrayList;
                            mAdapterListTrip.notifyDataSetChanged();
                        }
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
            client.AddHeader("username", mUserName);
            client.AddHeader("password", mPassword);
            client.AddHeader("wardid0", mSearchView.getQuery().toString());
            client.AddHeader("wardid1", "");
            client.AddHeader("wardid2", "");
            try {
                client.Execute(RequestMethod.POST);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response = client.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private AlertDialog createDialogError(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void saveLogCallToServer(String mobileCustomer, String mobileDriver,
                                    String timeYYYYMMDDhhmmss) {
        isConnectionExist = _checkConnect.checkData();
        String url = Config.URL_WS + "mobile/saveLogCallToServer";
        if (isConnectionExist) {
            try {
                RestClient client = new RestClient(url);
                client.AddHeader("mobileCustomer", mobileCustomer);
                client.AddHeader("mobileDriver", mobileDriver);
                try {
                    client.Execute(RequestMethod.POST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
