package gpg.internship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class MainActivity extends AppCompatActivity {

    Button login, signup;
    public static EditText username, password;
    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("GPGApp.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,FIRSTNAME VARCHAR(50),LASTNAME VARCHAR(50),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(20))";
        db.execSQL(tableQuery);

        username = findViewById(R.id.main_username);
        password = findViewById(R.id.main_password);

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
                        Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();

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
            }
        });

    }
}