package cn.edu.swufe.gupiao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.swufe.gupiao.R;

public class MyAdapter extends ArrayAdapter {

    private static final String TAG = "MyAdapter";

    public MyAdapter(Context context, int resource, ArrayList<HashMap<String,String>> list) {
        super(context, resource, list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_rate_list,parent,false);
        }

            HashMap<String,String> map = (HashMap<String, String>) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
        TextView detail = (TextView) itemView.findViewById(R.id.itemDetail);

        title.setText(11111+"");
        detail.setText(22222+"");

        return itemView;
    }
}