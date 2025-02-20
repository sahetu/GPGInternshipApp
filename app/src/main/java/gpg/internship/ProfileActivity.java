package gpg.internship;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    EditText firstName,lastName,email,contact,password,confirmPassword;
    RadioGroup gender;
    RadioButton male,female;
    Button submit,logout,edit,delete;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SQLiteDatabase db;
    String sGender;
    SharedPreferences sp;
    
    ApiInterface apiInterface;
    ProgressDialog pd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        
        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("GPGApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,FIRSTNAME VARCHAR(50),LASTNAME VARCHAR(50),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(20))";
        db.execSQL(tableQuery);

        firstName = findViewById(R.id.profile_firstname);
        lastName = findViewById(R.id.profile_lastname);
        email = findViewById(R.id.profile_email);
        contact = findViewById(R.id.profile_contacno);
        password = findViewById(R.id.profile_password);
        confirmPassword = findViewById(R.id.profile_retype_password);
        gender = findViewById(R.id.profile_gender);
        male = findViewById(R.id.profile_male);
        female = findViewById(R.id.profile_female);
        submit = findViewById(R.id.profile_submit);
        logout = findViewById(R.id.profile_logout);
        edit = findViewById(R.id.profile_edit);
        delete = findViewById(R.id.profile_delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Account Delete!");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Are You Sure Want To Delete Your Account!");

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //doSqliteDelete();
                        if(new ConnectionDetector(ProfileActivity.this).networkConnected()){
                            //new doDelete().execute();
                            pd = new ProgressDialog(ProfileActivity.this);
                            pd.setMessage("Please Wait...");
                            pd.setCancelable(false);
                            pd.show();
                            doDeleteRetrofit();
                        }
                        else{
                            new ConnectionDetector(ProfileActivity.this).networkDisconnected();
                        }
                    }
                });

                builder.show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Logout!");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Are You Sure Want To Logout!");

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sp.edit().clear().commit();
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ProfileActivity.this, "Rate Us", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                sGender = radioButton.getText().toString();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstName.getText().toString().trim().equals("")){
                    firstName.setError("First Name Required");
                }
                else if(lastName.getText().toString().trim().equals("")){
                    lastName.setError("Last Name Required");
                }
                else if(email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(contact.getText().toString().trim().equals("")){
                    contact.setError("Contact No. Required");
                }
                else if(contact.getText().toString().trim().length()<10){
                    contact.setError("Valid Contact No. Required");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password Required");
                }
                else if(password.getText().toString().trim().length()<6){
                    password.setError("Min. 6 Char Password Required");
                }
                else if(confirmPassword.getText().toString().trim().equals("")){
                    confirmPassword.setError("Retype Password Required");
                }
                else if(confirmPassword.getText().toString().trim().length()<6){
                    confirmPassword.setError("Min. 6 Char Retype Password Required");
                }
                else if(!password.getText().toString().trim().matches(confirmPassword.getText().toString().trim())){
                    confirmPassword.setError("Password Does Not Match");
                }
                else if(gender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(ProfileActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }
                else{
                    //doSqliteUpdate();
                    if(new ConnectionDetector(ProfileActivity.this).networkConnected()){
                        //new doUpdate().execute();
                        pd = new ProgressDialog(ProfileActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        doUpdateRetrofit();
                    }
                    else{
                        new ConnectionDetector(ProfileActivity.this).networkDisconnected();
                    }
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

        setData(false);

    }

    private void doDeleteRetrofit() {
        Call<GetSignupData> call = apiInterface.deleteProfileData(
                sp.getString(ConstantSp.USERID,"")
        );

        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        Toast.makeText(ProfileActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        sp.edit().clear().commit();
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(ProfileActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ProfileActivity.this, ConstantSp.SERVER_ERROR+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doUpdateRetrofit() {
        Call<GetSignupData> call = apiInterface.updateProfileData(
                firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString(),
                contact.getText().toString(),
                password.getText().toString(),
                sGender,
                sp.getString(ConstantSp.USERID,"")
        );

        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        Toast.makeText(ProfileActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        sp.edit().putString(ConstantSp.FIRSTNAME,firstName.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.LASTNAME,lastName.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.GENDER,sGender).commit();

                        setData(false);
                    }
                    else{
                        Toast.makeText(ProfileActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ProfileActivity.this, ConstantSp.SERVER_ERROR+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doSqliteDelete() {
        String deleteQuery = "DELETE FROM USERS WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
        db.execSQL(deleteQuery);
        sp.edit().clear().commit();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void doSqliteUpdate() {
        String updateQuery = "UPDATE USERS SET FIRSTNAME='"+firstName.getText().toString()+"',LASTNAME='"+lastName.getText().toString()+"',EMAIL='"+email.getText().toString()+"',CONTACT='"+contact.getText().toString()+"',PASSWORD='"+password.getText().toString()+"',GENDER='"+sGender+"' WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
        db.execSQL(updateQuery);
        Toast.makeText(ProfileActivity.this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();

        sp.edit().putString(ConstantSp.FIRSTNAME,firstName.getText().toString()).commit();
        sp.edit().putString(ConstantSp.LASTNAME,lastName.getText().toString()).commit();
        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
        sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
        sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();
        sp.edit().putString(ConstantSp.GENDER,sGender).commit();

        setData(false);
    }

    private void setData(boolean b) {
        if(b){
           edit.setVisibility(View.GONE);
           submit.setVisibility(View.VISIBLE);
           confirmPassword.setVisibility(View.VISIBLE);
        }
        else{
            edit.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            confirmPassword.setVisibility(View.GONE);
        }

        firstName.setEnabled(b);
        lastName.setEnabled(b);
        email.setEnabled(b);
        contact.setEnabled(b);
        password.setEnabled(b);
        confirmPassword.setEnabled(b);
        male.setEnabled(b);
        female.setEnabled(b);

        firstName.setText(sp.getString(ConstantSp.FIRSTNAME,""));
        lastName.setText(sp.getString(ConstantSp.LASTNAME,""));
        email.setText(sp.getString(ConstantSp.EMAIL,""));
        contact.setText(sp.getString(ConstantSp.CONTACT,""));
        password.setText(sp.getString(ConstantSp.PASSWORD,""));
        confirmPassword.setText(sp.getString(ConstantSp.PASSWORD,""));

        sGender = sp.getString(ConstantSp.GENDER,"");
        if(sGender.equalsIgnoreCase("Male")){
            male.setChecked(true);
        }
        else if(sGender.equalsIgnoreCase("Female")){
            female.setChecked(true);
        }
        else{

        }
    }

    private class doUpdate extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("firstname",firstName.getText().toString());
            hashMap.put("lastname",lastName.getText().toString());
            hashMap.put("email",email.getText().toString());
            hashMap.put("contact",contact.getText().toString());
            hashMap.put("password",password.getText().toString());
            hashMap.put("gender",sGender);
            hashMap.put("userid",sp.getString(ConstantSp.USERID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSp.UPDATE_URL,MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("status")){
                    Toast.makeText(ProfileActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    sp.edit().putString(ConstantSp.FIRSTNAME,firstName.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.LASTNAME,lastName.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.PASSWORD,password.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.GENDER,sGender).commit();

                    setData(false);
                }
                else{
                    Toast.makeText(ProfileActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class doDelete extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userid",sp.getString(ConstantSp.USERID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSp.DELETE_URL,MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("status")){
                    Toast.makeText(ProfileActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    sp.edit().clear().commit();
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(ProfileActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}