package gpg.internship;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SpinnerListActivity extends AppCompatActivity {

    Spinner spinner;
    //String[] countryArray = {"India","Australia","Canada","UK"};
    ArrayList<String> arrayList;

    GridView listView;

    AutoCompleteTextView autoTxt;
    MultiAutoCompleteTextView multiTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_list);

        spinner = findViewById(R.id.spinner);
        arrayList = new ArrayList<>();
        arrayList.add("India");
        arrayList.add("Australia");
        arrayList.add("UK");
        arrayList.add("Canada");
        arrayList.add("Demo");
        arrayList.add("Ireland");
        arrayList.add("Canaya");

        arrayList.remove(4);
        arrayList.set(5,"Kenya");

        arrayList.add(2,"France");

        ArrayAdapter adapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(SpinnerListActivity.this, countryArray[i], Toast.LENGTH_SHORT).show();
                Toast.makeText(SpinnerListActivity.this, arrayList.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView = findViewById(R.id.listview);
        ArrayAdapter listAdapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SpinnerListActivity.this, arrayList.get(i), Toast.LENGTH_SHORT).show();
            }
        });

        autoTxt = findViewById(R.id.autocomplete);
        ArrayAdapter autoAdapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,arrayList);
        autoTxt.setThreshold(1);
        autoTxt.setAdapter(autoAdapter);

        multiTxt = findViewById(R.id.multiautocomplete);
        ArrayAdapter multiautoAdapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,arrayList);
        multiTxt.setThreshold(1);
        multiTxt.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiTxt.setAdapter(multiautoAdapter);
    }
}