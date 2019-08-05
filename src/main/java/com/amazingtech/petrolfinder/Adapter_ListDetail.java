package com.amazingtech.petrolfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Amazing Ami on 24-06-2017.
 */

public class Adapter_ListDetail extends BaseAdapter {
    Context context;

    View view;
    LayoutInflater layoutInflater;
    ArrayList<HashMap<String,String>>datalist;
    Context contextf;
    TextView tvname,tvaddress,tvkm;
    Button btn_viewmap;

    public Adapter_ListDetail(Context context, ArrayList<HashMap<String,String>> datalist1) {
        contextf = context;
        datalist= datalist1;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datalist.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
       View vi=convertView;
        if (convertView == null) {

            vi = layoutInflater.inflate(R.layout.row_detail_list, null);
        }  btn_viewmap = (Button) vi.findViewById(R.id.btn_viewmap);
        tvname = (TextView) vi.findViewById(R.id.tvname);
        tvaddress = (TextView) vi.findViewById(R.id.tvaddress);
        tvkm = (TextView) vi.findViewById(R.id.tvkm);

        return vi;
    }

}
