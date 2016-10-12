package fanvu.easygoer.object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by SONY on 1/17/2016.
 */
public class TripInfo implements Serializable {
    @SerializedName("tripId")
    private String tripId;
    @SerializedName("tripPrice")
    private String tripPrice;
    @SerializedName("timeStart")
    private String timeStart;
    @SerializedName("placeStart")
    private String placeStart;
    @SerializedName("placeEnd")
    private String placeEnd;
    @SerializedName("nameDriver")
    private String nameDriver;
    @SerializedName("phone")
    private String phone;
    @SerializedName("comment")
    private String comment;
    @SerializedName("regId")
    private String registerId;

    public TripInfo(String tripId, String tripPrice, String timeStart, String placeStart,
                    String placeEnd, String nameDriver, String phone, String comment) {
        this.tripPrice = tripPrice;
        this.tripId = tripId;
        this.timeStart = timeStart;
        this.placeStart = placeStart;
        this.placeEnd = placeEnd;
        this.nameDriver = nameDriver;
        this.phone = phone;
        this.comment = comment;
    }

    public TripInfo() {
    }

    public String getTripPrice() {
        return tripPrice;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getPlaceStart() {
        return placeStart;
    }

    public String getPlaceEnd() {
        return placeEnd;
    }

    public String getNameDriver() {
        return nameDriver;
    }

    public String getPhone() {
        return phone;
    }

    public String getComment() {
        return comment;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setTripPrice(String tripPrice) {
        this.tripPrice = tripPrice;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setPlaceStart(String placeStart) {
        this.placeStart = placeStart;
    }

    public void setPlaceEnd(String placeEnd) {
        this.placeEnd = placeEnd;
    }

    public void setNameDriver(String nameDriver) {
        this.nameDriver = nameDriver;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
}
