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

public class Adapter_Search extends BaseAdapter {
    Context context;
    View view;
    TextView tvsearch;
    LayoutInflater layoutInflater=null;
    ArrayList<HashMap<String,String>>citilist;

    public Adapter_Search(Context contextf, ArrayList<HashMap<String,String>>dataf) {
        context = contextf;
        citilist = dataf;
        layoutInflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return citilist.size();
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

        if (convertView == null) {
            view = new View(context);
            view = layoutInflater.inflate(R.layout.row_search_list, null);
            tvsearch = (TextView) view.findViewById(R.id.tvsearch);
            HashMap<String,String>cmap=citilist.get(position);

            tvsearch.setText(cmap.get(SessionForm.Key_city_name));

        }
    return view;
    }


}
