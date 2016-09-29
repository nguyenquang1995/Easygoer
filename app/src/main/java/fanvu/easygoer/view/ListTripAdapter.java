package fanvu.easygoer.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fanvu.easygoer.gcm.R;
import fanvu.easygoer.TripInfo;

import java.util.ArrayList;

/**
 * Created by SONY on 1/17/2016.
 */
public class ListTripAdapter extends BaseAdapter {
    private int count = 0;
    private ArrayList<TripInfo> tripInfoArrayList;
    private static LayoutInflater inflater = null;
    Context context;

    public ListTripAdapter(Context context, ArrayList<TripInfo> tripInfoArrayList) {
        this.tripInfoArrayList = tripInfoArrayList;
        this.context = context;
        this.count = tripInfoArrayList.size();
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.trip_item, null);
            viewHolder = new ViewHolderItem();

            viewHolder.txtIndex = (TextView) convertView.findViewById(R.id.index);
            viewHolder.txtDriverName = (TextView) convertView.findViewById(R.id.driver_name);
            viewHolder.txtPhone = (TextView) convertView.findViewById(R.id.item_phone);
            viewHolder.imgPhoneCall = (ImageView) convertView.findViewById(R.id.imgPhone);
            viewHolder.txtTimeStart = (TextView) convertView.findViewById(R.id.time_start);
            viewHolder.txtPlaceStart = (TextView) convertView.findViewById(R.id.place_start);
            viewHolder.txtPlaceEnd = (TextView) convertView.findViewById(R.id.place_end);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.price);
            viewHolder.txtComment = (TextView) convertView.findViewById(R.id.item_comment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }
        viewHolder.txtIndex.setText(""+ (position + 1) + ". ");
        viewHolder.txtDriverName.setText(tripInfoArrayList.get(position).getNameDriver());
        viewHolder.txtPhone.setText(tripInfoArrayList.get(position).getPhone());
        viewHolder.txtTimeStart.setText(context.getResources().getString(R.string.string_time) + ": " +
                                                tripInfoArrayList.get(position).getTimeStart());
        viewHolder.txtPlaceStart.setText(context.getResources().getString(R.string.string_start_place) + ": " +
                                                 tripInfoArrayList.get(position).getPlaceStart());
        viewHolder.txtPlaceEnd.setText(context.getResources().getString(R.string.string_end_place) + ": " +
                                               tripInfoArrayList.get(position).getPlaceEnd());
        viewHolder.txtPrice.setText(context.getResources().getString(R.string.string_price) + ": " +
                tripInfoArrayList.get(position).getTripPrice());
        viewHolder.txtComment.setText(context.getResources().getString(R.string.string_comment) + ": " +
                                              tripInfoArrayList.get(position).getComment());
        return convertView;
    }
}

class ViewHolderItem {
    //    ImageView imgProfile;
    TextView txtIndex;
    TextView txtDriverName;
    ImageView imgPhoneCall;
    TextView txtPhone;
    TextView txtTimeStart;
    TextView txtPlaceStart;
    TextView txtPlaceEnd;
    TextView txtPrice;
    TextView txtComment;
}
