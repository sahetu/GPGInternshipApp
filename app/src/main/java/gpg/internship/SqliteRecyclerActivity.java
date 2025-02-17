package gpg.internship;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SqliteRecyclerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabase db;
    ArrayList<CustomList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_recycler);

        db = openOrCreateDatabase("GPGApp.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,FIRSTNAME VARCHAR(50),LASTNAME VARCHAR(50),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(20))";
        db.execSQL(tableQuery);

        recyclerView = findViewById(R.id.sqlite_recyclerview);
        //recyclerView.setLayoutManager(new LinearLayoutManager(SqliteRecyclerActivity.this));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView.setNestedScrollingEnabled(false);

        //doSqliteData();
        if(new ConnectionDetector(SqliteRecyclerActivity.this).networkConnected()){
            new getData().execute();
        }
        else{
            new ConnectionDetector(SqliteRecyclerActivity.this).networkDisconnected();
        }

    }

    private void doSqliteData() {
        String loginQuery = "SELECT * FROM USERS ORDER BY USERID DESC";
        Cursor cursor = db.rawQuery(loginQuery,null);
        if(cursor.getCount()>0){
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()){
                CustomList list = new CustomList();
                list.setUserId(cursor.getString(0));
                list.setFirstName(cursor.getString(1));
                list.setLastName(cursor.getString(2));
                list.setEmail(cursor.getString(3));
                list.setContact(cursor.getString(4));
                list.setGender(cursor.getString(6));
                arrayList.add(list);
            }
            UserRecyclerAdapter adapter = new UserRecyclerAdapter(SqliteRecyclerActivity.this,arrayList);
            recyclerView.setAdapter(adapter);
        }
        else{
            Toast.makeText(SqliteRecyclerActivity.this, "Users Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private class getData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(SqliteRecyclerActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            return new MakeServiceCall().MakeServiceCall(ConstantSp.GETDATA_URL,MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("status")){
                    JSONArray jsonArray = object.getJSONArray("UserDetails");
                    arrayList = new ArrayList<>();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CustomList list = new CustomList();
                        list.setUserId(jsonObject.getString("userId"));
                        list.setFirstName(jsonObject.getString("firstname"));
                        list.setLastName(jsonObject.getString("lastname"));
                        list.setEmail(jsonObject.getString("email"));
                        list.setContact(jsonObject.getString("contact"));
                        list.setGender(jsonObject.getString("gender"));
                        arrayList.add(list);
                    }
                    UserRecyclerAdapter adapter = new UserRecyclerAdapter(SqliteRecyclerActivity.this,arrayList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(SqliteRecyclerActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}