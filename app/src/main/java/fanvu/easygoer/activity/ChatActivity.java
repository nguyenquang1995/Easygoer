package fanvu.easygoer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import fanvu.easygoer.Constant;
import fanvu.easygoer.adapter.AdapterChat;
import fanvu.easygoer.asynctask.GcmSenderAsyncTask;
import fanvu.easygoer.common.NotificationUtils;
import fanvu.easygoer.gcm.Config;
import fanvu.easygoer.gcm.R;
import fanvu.easygoer.object.MessageObject;

public class ChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private List<MessageObject> mList = new ArrayList<>();
    private AdapterChat mAdapterChat;
    private EditText mEditText;
    private String mRegisterId;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MessageObject messageObject =
                new MessageObject(intent.getStringExtra(Config.EXTRA_MESSAGE), "");
            mList.add(messageObject);
            mAdapterChat.mList = mList;
            mAdapterChat.notifyDataSetChanged();
            NotificationUtils.clearNotification();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra(Constant.INTENT_USER_PHONE));
        mRegisterId = intent.getStringExtra(Constant.INTENT_REG_ID);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_chat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapterChat = new AdapterChat(getApplicationContext(), mList);
        mRecyclerView.setAdapter(mAdapterChat);
        mEditText = (EditText) findViewById(R.id.edt_message);
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText().toString().equals("")) {
                    return;
                }
                MessageObject messageObject =
                    new MessageObject(mEditText.getText().toString(), MessageObject.MY_OWN_MESSAGE);
                mList.add(messageObject);
                mAdapterChat.mList = mList;
                mAdapterChat.notifyDataSetChanged();
            }
        });
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GcmSenderAsyncTask asyncTask = new GcmSenderAsyncTask(mRegisterId, mEditText
                    .getText().toString());
                asyncTask.execute();
            }
        });
        registerReceiver(mBroadcastReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
