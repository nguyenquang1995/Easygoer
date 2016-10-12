package fanvu.easygoer.myinterface;

import fanvu.easygoer.object.ListTripResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by framgia on 12/10/2016.
 */
public interface ApiInterface {
    @FormUrlEncoded
    @POST("mobile/authenUser")
    Call<ListTripResponse> getListTrip(@Field("username") String userName
        , @Field("password") String password
        , @Field("wardid0") String wardid0
        , @Field("wardid1") String wardid1
        , @Field("wardid2") String wardid2);
}
