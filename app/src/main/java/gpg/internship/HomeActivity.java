package gpg.internship;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    TextView username,password;
    //RadioButton male,female;
    RadioGroup gender;
    CheckBox cricket,football,chess;
    Button show;
    StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = findViewById(R.id.home_username);
        password = findViewById(R.id.home_password);

        /*username.setText(MainActivity.username.getText().toString());
        password.setText(MainActivity.password.getText().toString());*/

        /*Bundle bundle = getIntent().getExtras();
        username.setText(bundle.getString("USERNAME"));
        password.setText(bundle.getString("PASSWORD"));*/

        /*male = findViewById(R.id.home_male);
        female = findViewById(R.id.home_female);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, male.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, female.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

        gender = findViewById(R.id.home_gender);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                Toast.makeText(HomeActivity.this, radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        cricket = findViewById(R.id.home_cricket);
        football = findViewById(R.id.home_football);
        chess = findViewById(R.id.home_chess);

        /*cricket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cricket.isChecked()) {
                    Toast.makeText(HomeActivity.this, cricket.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        football.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    Toast.makeText(HomeActivity.this, football.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        chess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    Toast.makeText(HomeActivity.this, chess.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        show = findViewById(R.id.home_show);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb = new StringBuilder();

                if(cricket.isChecked()){
                    //Toast.makeText(HomeActivity.this, cricket.getText().toString(), Toast.LENGTH_SHORT).show();
                    sb.append(cricket.getText().toString()+"\n");
                }
                if(football.isChecked()){
                    //Toast.makeText(HomeActivity.this, football.getText().toString(), Toast.LENGTH_SHORT).show();
                    sb.append(football.getText().toString()+"\n");
                }
                if(chess.isChecked()){
                    //Toast.makeText(HomeActivity.this, chess.getText().toString(), Toast.LENGTH_SHORT).show();
                    sb.append(chess.getText().toString()+"\n");
                }

                if(sb.toString().trim().equals("")){
                    Toast.makeText(HomeActivity.this, "Please Select Checkbox", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(HomeActivity.this, sb.toString().trim(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}