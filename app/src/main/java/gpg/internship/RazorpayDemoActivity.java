package gpg.internship;

import android.app.Activity;
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

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RazorpayDemoActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    EditText amount;
    Button payNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay_demo);

        amount = findViewById(R.id.amount);
        payNow = findViewById(R.id.pay_now);

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getText().toString().trim().equals("") || amount.getText().toString().trim().equals("0")){
                    amount.setError("Amount Required");
                }
                else{
                    startPayment();
                }
            }
        });

    }

    private void startPayment() {
        Activity activity = this;
        Checkout co = new Checkout();
        co.setKeyID("rzp_test_xsiOz9lYtWKHgF");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",getResources().getString(R.string.app_name));
            jsonObject.put("description","Desc");
            jsonObject.put("send_sms_hash",true);
            jsonObject.put("allow_rotation",true);

            jsonObject.put("image",R.mipmap.ic_launcher);
            jsonObject.put("currency","INR");
            jsonObject.put("amount",String.valueOf(Integer.parseInt(amount.getText().toString())*100));

            JSONObject prefill = new JSONObject();
            prefill.put("email","sagar@gmail.com");
            prefill.put("contact","7433050707");

            jsonObject.put("prefill",prefill);
            co.open(activity,jsonObject);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Log.d("RESPONSE_SUCCESS",s);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Log.d("RESPONSE_FAIL",s);
    }
}