package gpg.internship;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeActivity extends AppCompatActivity {

    EditText dateEdit,timeEdit;
    Calendar calendar;
    int iHour = 0, iMinute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);
        dateEdit = findViewById(R.id.date_edit);
        timeEdit = findViewById(R.id.time_edit);

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                dateEdit.setText(dateFormat.format(calendar.getTime()));

            }
        };

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog  = new DatePickerDialog(DateTimeActivity.this,dateClick,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        TimePickerDialog.OnTimeSetListener timeClick = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                iHour = i;
                iMinute = i1;

                String sAMPM= "";
                if(iHour>12){
                    sAMPM = "PM";
                    iHour -=12;
                    //iHour = iHour-12;
                }
                else if(iHour==12){
                    sAMPM = "PM";
                }
                else if(iHour==0){
                    sAMPM = "AM";
                    iHour = 12;
                }
                else{
                    sAMPM = "AM";
                }

                String sMinute = "";
                if(iMinute<10){
                    sMinute = "0"+iMinute;
                }
                else{
                    sMinute = String.valueOf(iMinute);
                }

                timeEdit.setText(iHour+" : "+sMinute+" "+sAMPM);
            }
        };

        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(DateTimeActivity.this,timeClick,iHour,iMinute,false).show();
            }
        });

    }
}