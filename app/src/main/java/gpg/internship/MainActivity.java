package gpg.internship;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button login, signup,usersList,recyclerButton;
    public static EditText username, password;
    SQLiteDatabase db;
    SharedPreferences sp;
    ApiInterface apiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("GPGApp.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,FIRSTNAME VARCHAR(50),LASTNAME VARCHAR(50),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(20))";
        db.execSQL(tableQuery);

        username = findViewById(R.id.main_username);
        password = findViewById(R.id.main_password);

        recyclerButton = findViewById(R.id.main_recycler);
        recyclerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SqliteRecyclerActivity.class);
                startActivity(intent);
            }
        });

        usersList = findViewById(R.id.main_user);

        usersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UserListViewActivity.class);
                startActivity(intent);
            }
        });

        signup = findViewById(R.id.main_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login = findViewById(R.id.main_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().trim().equals("")) {
                    //username.setError(getResources().getString(R.string.login));
                    username.setError("Username Required");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                } else if (password.getText().toString().trim().length() < 6) {
                    password.setError("Min. 6 Char Password Required");
                } else {
                    //doSqliteLogin();
                    if(new ConnectionDetector(MainActivity.this).networkConnected()){
                        //new doLogin().execute();
                        pd = new ProgressDialog(MainActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        doLoginRetrofit();
                    }
                    else{
                        new ConnectionDetector(MainActivity.this).networkDisconnected();
                    }
                }
            }
        });

    }

    private void doLoginRetrofit() {
        Call<GetLoginData> call = apiInterface.getLoginData(username.getText().toString(),password.getText().toString());
        call.enqueue(new Callback<GetLoginData>() {
            @Override
            public void onResponse(Call<GetLoginData> call, Response<GetLoginData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        Toast.makeText(MainActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        for (int i=0;i<response.body().userDetails.size();i++){
                            String sUserId = response.body().userDetails.get(i).userId;
                            String sFirstName = response.body().userDetails.get(i).firstname;
                            String sLastName = response.body().userDetails.get(i).lastname;
                            String sEmail = response.body().userDetails.get(i).email;
                            String sContact = response.body().userDetails.get(i).contact;
                            String sPassword = "";
                            String sGender = response.body().userDetails.get(i).gender;
                            String sProfile = response.body().userDetails.get(i).profile;

                            sp.edit().putString(ConstantSp.USERID,sUserId).commit();
                            sp.edit().putString(ConstantSp.FIRSTNAME,sFirstName).commit();
                            sp.edit().putString(ConstantSp.LASTNAME,sLastName).commit();
                            sp.edit().putString(ConstantSp.EMAIL,sEmail).commit();
                            sp.edit().putString(ConstantSp.CONTACT,sContact).commit();
                            sp.edit().putString(ConstantSp.PASSWORD,sPassword).commit();
                            sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                            sp.edit().putString(ConstantSp.PROFILE_IMAGE,sProfile).commit();
                        }
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(MainActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, ConstantSp.SERVER_ERROR+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetLoginData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doSqliteLogin() {
        String loginQuery = "SELECT * FROM USERS WHERE (EMAIL='"+username.getText().toString()+"' OR CONTACT='"+username.getText().toString()+"') AND PASSWORD='"+password.getText().toString()+"'";
        Cursor cursor = db.rawQuery(loginQuery,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String sUserId = cursor.getString(0);
                String sFirstName = cursor.getString(1);
                String sLastName = cursor.getString(2);
                String sEmail = cursor.getString(3);
                String sContact = cursor.getString(4);
                String sPassword = cursor.getString(5);
                String sGender = cursor.getString(6);

                sp.edit().putString(ConstantSp.USERID,sUserId).commit();
                sp.edit().putString(ConstantSp.FIRSTNAME,sFirstName).commit();
                sp.edit().putString(ConstantSp.LASTNAME,sLastName).commit();
                sp.edit().putString(ConstantSp.EMAIL,sEmail).commit();
                sp.edit().putString(ConstantSp.CONTACT,sContact).commit();
                sp.edit().putString(ConstantSp.PASSWORD,sPassword).commit();
                sp.edit().putString(ConstantSp.GENDER,sGender).commit();
            }
            System.out.println("Login Successfully");
            Log.d("RESPONSE", "Login Successfully");
            Log.e("RESPONSE", "Login Successfully");
            Log.w("RESPONSE", "Login Successfully");
            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
            //Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

                        /*Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("USERNAME", username.getText().toString());
                        bundle.putString("PASSWORD", password.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);*/
        }
        else{
            Toast.makeText(MainActivity.this, "Invalid Credential", Toast.LENGTH_SHORT).show();
        }
    }

    private class doLogin extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("email",username.getText().toString());
            hashMap.put("password",password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSp.LOGIN_URL,MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("status")){
                    Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = object.getJSONArray("UserDetails");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String sUserId = jsonObject.getString("userId");
                        String sFirstName = jsonObject.getString("firstname");
                        String sLastName = jsonObject.getString("lastname");
                        String sEmail = jsonObject.getString("email");
                        String sContact = jsonObject.getString("contact");
                        String sPassword = "";
                        String sGender = jsonObject.getString("gender");

                        sp.edit().putString(ConstantSp.USERID,sUserId).commit();
                        sp.edit().putString(ConstantSp.FIRSTNAME,sFirstName).commit();
                        sp.edit().putString(ConstantSp.LASTNAME,sLastName).commit();
                        sp.edit().putString(ConstantSp.EMAIL,sEmail).commit();
                        sp.edit().putString(ConstantSp.CONTACT,sContact).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,sPassword).commit();
                        sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                    }
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}