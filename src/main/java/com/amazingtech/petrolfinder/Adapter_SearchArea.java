package com.amazingtech.petrolfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Amazing Ami on 26-06-2017.
 */

public class Adapter_SearchArea extends BaseAdapter {
    Context context;
    View view;
    TextView tvareasearch;
    LayoutInflater layoutInflater=null;
    ArrayList<HashMap<String,String>>arealist;

    public Adapter_SearchArea(Context contextf, ArrayList<HashMap<String,String>>dataf) {
        context = contextf;
        arealist = dataf;
        layoutInflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arealist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup Parent) {
        View view = convertView;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.row_areasearch_list, null);
        }
            tvareasearch = (TextView) view.findViewById(R.id.tvareasearch);
            HashMap<String,String>cmap=arealist.get(position);

            tvareasearch.setText(cmap.get(SessionForm.Key_area_name));


    return view;
    }


}
