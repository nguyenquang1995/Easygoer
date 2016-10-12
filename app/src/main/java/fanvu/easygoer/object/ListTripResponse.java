package fanvu.easygoer.object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by framgia on 12/10/2016.
 */
public class ListTripResponse {
    @SerializedName("lstTrip")
    List<TripInfo> results = new ArrayList<>();
    @SerializedName("typeUser")
    String typeUser;

    public List<TripInfo> getResults() {
        return results;
    }

    public String getTypeUser() {
        return typeUser;
    }
}
