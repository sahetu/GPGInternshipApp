package gpg.internship;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLoginData {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("UserDetails")
    @Expose
    public List<GetUserDetail> userDetails;

    public class GetUserDetail {
        @SerializedName("userId")
        @Expose
        public String userId;
        @SerializedName("firstname")
        @Expose
        public String firstname;
        @SerializedName("lastname")
        @Expose
        public String lastname;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("contact")
        @Expose
        public String contact;
        @SerializedName("gender")
        @Expose
        public String gender;
    }
}
