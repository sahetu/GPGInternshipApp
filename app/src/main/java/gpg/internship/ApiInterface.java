package gpg.internship;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("signup.php")
    Call<GetSignupData> getSignupData(
            @Field("firstname") String firstName,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("contact") String contact,
            @Field("password") String password,
            @Field("gender") String gender
    );

}
