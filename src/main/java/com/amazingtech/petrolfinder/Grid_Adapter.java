package com.amazingtech.petrolfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Amazing Ami on 24-06-2017.
 */

public class Grid_Adapter extends BaseAdapter {
    Context context;

    View view;
    LayoutInflater layoutInflater;
    ArrayList<HashMap<String,String>>datalist;
    Context contextf;
   /* int[] imageId;*/


    public Grid_Adapter(Context context, ArrayList<HashMap<String,String>> datalist1) {
        contextf = context;
        datalist= datalist1;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //this.imageId = imageId;
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

            vi = layoutInflater.inflate(R.layout.single_itemgrid, null);
        }
            ImageView ivsingle = (ImageView) vi.findViewById(R.id.ivsingle);
            TextView tv_single = (TextView) vi.findViewById(R.id.tv_single);
            HashMap<String,String>cmap=datalist.get(position);


          /* String catnam= cmap.get("CategoryName");
        btnsingle.setText(catnam);
        ivsingle.setBackgroundResource(imageId[position]);*/

        String catnam= cmap.get(SessionForm.Key_category_name);
        String imageurl=cmap.get(SessionForm.Key_category_icon);
        tv_single.setText(catnam);

        Picasso.with(context)
                .load( imageurl)
                .error(R.drawable.ic_fogg)
                .placeholder(R.drawable.slider_1visa)
                .into(ivsingle);

        return vi;
    }
}
