package fanvu.easygoer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SONY on 1/17/2016.
 */
public class TripInfoArray implements Serializable {

    private ArrayList<TripInfo> tripInfoArrayList;

    public TripInfoArray(ArrayList<TripInfo> data) {
        this.tripInfoArrayList = data;
    }

    public ArrayList<TripInfo> getTripInfoArray() {
        return this.tripInfoArrayList;
    }
}
