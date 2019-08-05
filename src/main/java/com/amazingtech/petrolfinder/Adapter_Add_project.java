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
 * Created by mayankthakur on 6/27/2017.
 */
public class Adapter_Add_project extends BaseAdapter {


    Context ctx;
    ArrayList<HashMap<String, String>> arrcityf;
    LayoutInflater inflater = null;


    public Adapter_Add_project(Context ctxf, ArrayList<HashMap<String, String>>arrcity) {
       ctx = ctxf;
       arrcityf = arrcity;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    
    @Override
    public int getCount() {
        return arrcityf.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View   v = inflater.inflate(R.layout.row_addproject, null);

    TextView catogery_name=(TextView)v.findViewById(R.id.catogery_name);


        HashMap<String, String> cmap = new HashMap<String, String>();

        cmap = arrcityf.get(position);

        String catname = cmap.get(SessionForm.Key_category_name);

        catogery_name.setText(catname);


       // String imageurl=cmap.get(SessionForm.Key_category_icon);


        return v;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
}
