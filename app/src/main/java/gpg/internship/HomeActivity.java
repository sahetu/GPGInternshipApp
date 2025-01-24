package gpg.internship;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    TextView username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = findViewById(R.id.home_username);
        password = findViewById(R.id.home_password);

        /*username.setText(MainActivity.username.getText().toString());
        password.setText(MainActivity.password.getText().toString());*/

        Bundle bundle = getIntent().getExtras();
        username.setText(bundle.getString("USERNAME"));
        password.setText(bundle.getString("PASSWORD"));

    }
}