package fanvu.easygoer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fanvu.easygoer.Constant;
import fanvu.easygoer.activity.ChatActivity;
import fanvu.easygoer.adapter.AdapterListTrip;
import fanvu.easygoer.common.CheckConnect;
import fanvu.easygoer.common.RequestMethod;
import fanvu.easygoer.common.RestClient;
import fanvu.easygoer.config.Config;
import fanvu.easygoer.gcm.R;
import fanvu.easygoer.myinterface.ApiInterface;
import fanvu.easygoer.mylistener.ItemTripClickListener;
import fanvu.easygoer.object.ApiClient;
import fanvu.easygoer.object.ListTripResponse;
import fanvu.easygoer.object.TripInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by framgia on 10/10/2016.
 */
public class HomeFragment extends Fragment implements ItemTripClickListener {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_trip, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listView);
        mSearchView = (SearchView) view.findViewById(R.id.searchview);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setTitle("Searching...");
        _checkConnect = new CheckConnect(getActivity());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant
                .SHARE_PREFERENCE,
            Context.MODE_PRIVATE);
        mUserName = sharedPreferences.getString(Constant.SHARE_USER_PHONE, "");
        mPassword = sharedPreferences.getString(Constant.SHARE_PASSWORD, "");
        mTypeOfUser = sharedPreferences.getString(Constant.SHARE_TYPE_USER, "");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapterListTrip = new AdapterListTrip(getActivity().getApplicationContext(),
            SearchTripInfoArrayList);
        mAdapterListTrip.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapterListTrip);
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

    private AlertDialog createDialog(final String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Gọi điện thoại: ");
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.text_call, new DialogInterface.OnClickListener() {
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
        builder.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void doSearching() {
        pDialog.show();
        ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);
        Call<ListTripResponse> call = apiService.getListTrip(mUserName
            , mPassword
            , mSearchView.getQuery().toString()
            , ""
            , "");
        call.enqueue(new Callback<ListTripResponse>() {
            @Override
            public void onResponse(Call<ListTripResponse> call,
                                   Response<ListTripResponse> response) {
                SearchTripInfoArrayList = (ArrayList) response.body().getResults();
                mAdapterListTrip.mList = SearchTripInfoArrayList;
                mAdapterListTrip.notifyDataSetChanged();
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ListTripResponse> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getActivity(), R.string.string_search_fail, Toast.LENGTH_SHORT)
                    .show();
            }
        });
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

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.imgPhone:
                Dialog dialog = createDialog(SearchTripInfoArrayList.get(position).getPhone());
                dialog.show();
                break;
            case R.id.btn_message:
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Constant.INTENT_USER_PHONE, SearchTripInfoArrayList.get(position)
                    .getPhone());
                intent.putExtra(Constant.INTENT_REG_ID, SearchTripInfoArrayList.get(position)
                    .getRegisterId());
                startActivity(intent);
        }
    }
}
