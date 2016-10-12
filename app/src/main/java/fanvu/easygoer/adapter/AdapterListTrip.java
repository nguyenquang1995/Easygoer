package fanvu.easygoer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fanvu.easygoer.object.TripInfo;
import fanvu.easygoer.gcm.R;
import fanvu.easygoer.mylistener.ItemTripClickListener;

/**
 * Created by framgia on 06/10/2016.
 */
public class AdapterListTrip extends RecyclerView.Adapter<AdapterListTrip.TripViewHolder> {
    public Context mContext;
    public List mList = new ArrayList();
    private ItemTripClickListener mItemTripClickListener;

    public AdapterListTrip(Context context, List list) {
        mContext = context;
        mList = list;
    }

    public void setOnItemClickListener(ItemTripClickListener listener) {
        mItemTripClickListener = listener;
    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TripViewHolder viewHolder, int position) {
        TripInfo tripInfo = (TripInfo) mList.get(position);
        viewHolder.txtIndex.setText("" + (position + 1) + ". ");
        viewHolder.txtDriverName.setText(tripInfo.getNameDriver());
        viewHolder.txtPhone.setText(tripInfo.getPhone());
        viewHolder.txtTimeStart
            .setText(mContext.getResources().getString(R.string.string_time) + ": " +
                tripInfo.getTimeStart());
        viewHolder.txtPlaceStart
            .setText(mContext.getResources().getString(R.string.string_start_place) + ": " +
                tripInfo.getPlaceStart());
        viewHolder.txtPlaceEnd
            .setText(mContext.getResources().getString(R.string.string_end_place) + ": " +
                tripInfo.getPlaceEnd());
        viewHolder.txtPrice
            .setText(mContext.getResources().getString(R.string.string_price) + ": " +
                tripInfo.getTripPrice());
        viewHolder.txtComment
            .setText(mContext.getResources().getString(R.string.string_comment) + ": " +
                tripInfo.getComment());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtIndex;
        TextView txtDriverName;
        ImageView imgPhoneCall;
        TextView txtPhone;
        TextView txtTimeStart;
        TextView txtPlaceStart;
        TextView txtPlaceEnd;
        TextView txtPrice;
        TextView txtComment;

        public TripViewHolder(View itemView) {
            super(itemView);
            txtIndex = (TextView) itemView.findViewById(R.id.index);
            txtDriverName = (TextView) itemView.findViewById(R.id.driver_name);
            txtPhone = (TextView) itemView.findViewById(R.id.item_phone);
            imgPhoneCall = (ImageView) itemView.findViewById(R.id.imgPhone);
            txtTimeStart = (TextView) itemView.findViewById(R.id.time_start);
            txtPlaceStart = (TextView) itemView.findViewById(R.id.place_start);
            txtPlaceEnd = (TextView) itemView.findViewById(R.id.place_end);
            txtPrice = (TextView) itemView.findViewById(R.id.price);
            txtComment = (TextView) itemView.findViewById(R.id.item_comment);
            imgPhoneCall.setOnClickListener(this);
            itemView.findViewById(R.id.btn_message).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemTripClickListener != null) {
                mItemTripClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
