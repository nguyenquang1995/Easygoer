package fanvu.easygoer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fanvu.easygoer.gcm.R;
import fanvu.easygoer.object.MessageObject;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by framgia on 13/10/2016.
 */
public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ChatViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;
    public Context mContext;
    public List mList = new ArrayList();

    public AdapterChat(Context context, List list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        MessageObject messageObject = (MessageObject) mList.get(position);
        if (messageObject.getSender().equals(MessageObject.MY_OWN_MESSAGE)) {
            return VIEW_TYPE_ME;
        }
        return VIEW_TYPE_OTHER;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_ME) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_chat, parent, false);
            return new ChatViewHolder(itemView);
        } else {
            itemView =
                LayoutInflater.from(mContext).inflate(R.layout.item_chat_friend, parent, false);
            return new ChatViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        MessageObject messageObject = (MessageObject) mList.get(position);
        if (messageObject.getSender().equals(MessageObject.MY_OWN_MESSAGE)) {
            Glide.with(mContext)
                .load(R.drawable.ic_launcher)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.avatar);
        } else {
            Glide.with(mContext)
                .load(R.drawable.ic_message_black_36dp)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.avatar);
        }
        holder.mTextView.setText(messageObject.getContent());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView mTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.im_avatar);
            mTextView = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }
}
