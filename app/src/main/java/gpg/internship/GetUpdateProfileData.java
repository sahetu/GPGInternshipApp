package gpg.internship;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUpdateProfileData {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("Profile")
    @Expose
    public String Profile;

}
