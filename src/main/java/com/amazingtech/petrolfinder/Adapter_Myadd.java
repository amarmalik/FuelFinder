package com.amazingtech.petrolfinder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amazing Ami on 08-07-2017.
 */

public class Adapter_Myadd extends BaseAdapter {
    Button btnupdate, btndelete;
    Context ctx;
    String selectedaddid="";
    String selectedaddtitle="";
    ImageView ivproductview1;
    ArrayList<HashMap<String, String>> myaddlistf;
    LayoutInflater inflater = null;

    Adapter_Myadd(Context ctxf, ArrayList<HashMap<String, String>> myaddlist)

    {
        ctx = ctxf;
        myaddlistf = myaddlist;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return myaddlistf.size();
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
        View vi = convertView;
        if (convertView == null) {

            vi = inflater.inflate(R.layout.row_myadd_list, null);
        }
        btndelete = (Button) vi.findViewById(R.id.btndelete);
        btnupdate = (Button) vi.findViewById(R.id.btnupdate);
        btnupdate.setOnClickListener(new clickbtnupdate(position));
        btndelete.setOnClickListener(new clickbtndelete(position));


        TextView tvtitle = (TextView) vi.findViewById(R.id.tvtitle);
        TextView tvaddress = (TextView) vi.findViewById(R.id.tvaddress);
        TextView tvname = (TextView) vi.findViewById(R.id.tvname);
        TextView tvmobileno = (TextView) vi.findViewById(R.id.tvmobileno);
        TextView tvlatt = (TextView) vi.findViewById(R.id.tvlatt);
        TextView tvlong = (TextView) vi.findViewById(R.id.tvlong);
        ivproductview1 = (ImageView) vi.findViewById(R.id.ivproductview1);
        HashMap<String, String> cmap = new HashMap<String, String>();
        cmap = myaddlistf.get(position);
        String strtitle = cmap.get(SessionForm.KEY_add_title);
        String strmobile = cmap.get(SessionForm.KEY_owner_mobile);
        String straddress = cmap.get(SessionForm.KEY_add_address);
        String strname = cmap.get(SessionForm.KEY_owner_name);
        String strlatt = cmap.get(SessionForm.KEY_add_latt);
        String strlong = cmap.get(SessionForm.KEY_add_long);


        tvaddress.setText(straddress);
        tvtitle.setText(strtitle);
        tvname.setText(strname);
        tvmobileno.setText(strmobile);
        tvlatt.setText(strlatt);
        tvlong.setText(strlong);

        String imageurl = cmap.get(SessionForm.KEY_add_image);
        Picasso.with(ctx)
                .load(imageurl)
                .error(R.drawable.slider_1visa)
                .placeholder(R.drawable.ic_search)
                .into(ivproductview1);


        return vi;


    }

   class clickbtnupdate implements View.OnClickListener
   {
       int pos=0;
clickbtnupdate(int postion)
{
    pos=postion;

}
       @Override
       public void onClick(View v) {

           SessionForm.selectedmap=myaddlistf.get(pos);
           Intent intent=new Intent(ctx,Activity_Updatepost.class);
           ctx.startActivity(intent);

       }
   }

    public class clickbtndelete implements View.OnClickListener

    {
        int cpos=0;
        clickbtndelete(int pos)
        {
            cpos=pos;
        }


        @Override
        public void onClick(View v) {
            SessionForm.selectedmap=myaddlistf.get(cpos);


            selectedaddid=SessionForm.selectedmap.get(SessionForm.KEY_add_id);
            selectedaddtitle=SessionForm.selectedmap.get(SessionForm.KEY_add_title);
            customalertconfirm("Do you really want to delete given add?");
        }
    }

    public void customalertconfirm(String msg)
    {
        final Dialog dialog=new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alertconfirm);
        TextView txtmsg=(TextView)dialog.findViewById(R.id.txtmessage);
        Button btnok=(Button)dialog.findViewById(R.id.btnok1);
        Button btnno=(Button)dialog.findViewById(R.id.btnno);
        txtmsg.setText(msg);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonObjReq2();

                dialog.dismiss();

            }
        });
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }
    public void customalert(String msg)
    {
        final Dialog dialog=new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog);
        TextView txtmsg=(TextView)dialog.findViewById(R.id.txmsg);
        Button btnok=(Button)dialog.findViewById(R.id.btnok);
        txtmsg.setText(msg);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();

    }

    private void makeJsonObjReq2() {
        //---For Display Progressbar
        final ProgressDialog pb=new ProgressDialog(ctx);
        pb.setMessage("Please wait..");
        pb.show();
//--Replace your url

        final String urlweb= Url.GetUrl("http://ezeonsoft.in/fuelfinder/deleteadd.php?"+"add_id="+selectedaddid);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlweb, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject result) {
                        Log.d("ressult", result.toString());
                        Log.d("url", urlweb);

//--Dissmiss progress dialog
                        pb.dismiss();
//---Try catch block and your other parsing

                        try
                        {
//
                            JSONObject jobresponse=result.getJSONObject("response");

                            String status = jobresponse.getString("status");
                            String message = jobresponse.getString("message");

                            if(status.equals("1"))
                            {

                                Toast.makeText(ctx,message,Toast.LENGTH_LONG).show();

                                Intent i=new Intent(ctx,ActivityMenuLayout.class);
                                ctx.startActivity(i);
                              //  ctx.finish();
                            }
                            else
                            {
                                customalert(message);

                            }






                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();


                        }


                        
//----
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Userchoice", "Error: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


        };

        // Adding request to request queue
        String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
        MyApplication.getInstance().getRequestQueue().getCache().remove(urlweb);
        MyApplication.getInstance().addToRequestQueue(jsonObjReq,



                tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


}