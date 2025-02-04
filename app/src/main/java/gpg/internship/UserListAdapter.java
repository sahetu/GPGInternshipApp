package gpg.internship;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserListAdapter extends BaseAdapter {

    Context context;
    ArrayList<CustomList> arrayList;

    public UserListAdapter(Context context, ArrayList<CustomList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_list,null);
        TextView name = view.findViewById(R.id.custom_list_name);
        TextView email = view.findViewById(R.id.custom_list_email);
        TextView contact = view.findViewById(R.id.custom_list_contact);

        name.setText(arrayList.get(i).getFirstName()+" "+arrayList.get(i).getLastName()+" ("+arrayList.get(i).getGender()+")");
        email.setText(arrayList.get(i).getEmail());
        contact.setText(arrayList.get(i).getContact());

        return view;
    }
}
