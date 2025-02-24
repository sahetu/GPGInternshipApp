package gpg.internship;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    @FormUrlEncoded
    @POST("login.php")
    Call<GetLoginData> getLoginData(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("updateProfile.php")
    Call<GetSignupData> updateProfileData(
            @Field("firstname") String firstName,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("contact") String contact,
            @Field("password") String password,
            @Field("gender") String gender,
            @Field("userid") String userid
    );

    @FormUrlEncoded
    @POST("deleteProfile.php")
    Call<GetSignupData> deleteProfileData(
            @Field("userid") String userid
    );

    @GET("getAllData.php")
    Call<GetLoginData> getAllData();

    @Multipart
    @POST("updateProfileImage.php")
    Call<GetUpdateProfileData> updateProfileImageData(
            @Part("firstname") RequestBody firstname,
            @Part("lastname") RequestBody lastname,
            @Part("email") RequestBody email,
            @Part("contact") RequestBody contact,
            @Part("password") RequestBody password,
            @Part("gender") RequestBody gender,
            @Part("userid") RequestBody userid,
            @Part MultipartBody.Part image
    );

}
