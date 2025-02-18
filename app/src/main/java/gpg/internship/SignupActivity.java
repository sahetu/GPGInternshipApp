package gpg.internship;

import android.app.ProgressDialog;
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

public class SignupActivity extends AppCompatActivity {

    EditText firstName,lastName,email,contact,password,confirmPassword;
    RadioGroup gender;
    CheckBox checkBox;
    Button signup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SQLiteDatabase db;
    String sGender;
    ApiInterface apiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        db = openOrCreateDatabase("GPGApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,FIRSTNAME VARCHAR(50),LASTNAME VARCHAR(50),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(20))";
        db.execSQL(tableQuery);

        firstName = findViewById(R.id.signup_firstname);
        lastName = findViewById(R.id.signup_lastname);
        email = findViewById(R.id.signup_email);
        contact = findViewById(R.id.signup_contacno);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_retype_password);
        gender = findViewById(R.id.signup_gender);
        checkBox = findViewById(R.id.signup_terms);
        signup = findViewById(R.id.signup_button);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                sGender = radioButton.getText().toString();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(SignupActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }
                else if(!checkBox.isChecked()){
                    Toast.makeText(SignupActivity.this, "Please Accept Terms And Conditions", Toast.LENGTH_SHORT).show();
                }
                else{
                    //doSqliteSignup();
                    if(new ConnectionDetector(SignupActivity.this).networkConnected()){
                        //Toast.makeText(SignupActivity.this, "Internet/Wifi Connected", Toast.LENGTH_SHORT).show();
                        //new doSignup().execute();
                        pd = new ProgressDialog(SignupActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        doSignupRetrofit();
                    }
                    else{
                        new ConnectionDetector(SignupActivity.this).networkDisconnected();
                    }
                }
            }
        });

    }

    private void doSignupRetrofit() {
        Call<GetSignupData> call = apiInterface.getSignupData(
                firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString(),
                contact.getText().toString(),
                password.getText().toString(),
                sGender
        );

        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        Toast.makeText(SignupActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else{
                        Toast.makeText(SignupActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SignupActivity.this, ConstantSp.SERVER_ERROR+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void doSqliteSignup() {
        String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' OR CONTACT='"+contact.getText().toString()+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            Toast.makeText(SignupActivity.this, "Email Id/Contact No. ALready Exists", Toast.LENGTH_SHORT).show();
        }
        else{
            String insertQuery = "INSERT INTO USERS VALUES (NULL,'"+firstName.getText().toString()+"','"+lastName.getText().toString()+"','"+email.getText().toString()+"','"+contact.getText().toString()+"','"+password.getText().toString()+"','"+sGender+"')";
            db.execSQL(insertQuery);
            Toast.makeText(SignupActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private class doSignup extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SignupActivity.this);
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
            return new MakeServiceCall().MakeServiceCall(ConstantSp.SIGNUP_URL,MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("status")){
                    Toast.makeText(SignupActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else{
                    Toast.makeText(SignupActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}