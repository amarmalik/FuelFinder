package com.amazingtech.petrolfinder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_MyAdd extends AppCompatActivity {
    ListView listupdatepost;
    ImageView iv_updatepost_back;
    ArrayList<HashMap<String,String>> listf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my_add);
        listupdatepost=(ListView)findViewById(R.id.listupdatepost);
        iv_updatepost_back=(ImageView)findViewById(R.id.iv_updatepost_back);

        listf=new ArrayList<HashMap<String,String>>();
        iv_updatepost_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Activity_MyAdd.this,ActivityMenuLayout.class);
                startActivity(i);
                finish();
            }
        });


        makeJsonObjReq2();


    }
    private void makeJsonObjReq2() {
        //---For Display Progressbar
        final ProgressDialog pb = new ProgressDialog(Activity_MyAdd.this);

        pb.setMessage("Please wait..");
        pb.show();
//--Replace your url
        final String urlweb = Url.GetUrl("http://ezeonsoft.in/fuelfinder/view_myadd.php?"+"user_id="+SessionForm.GetSharedPreferences(SessionForm.KEY_user_id,Activity_MyAdd.this));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlweb, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
//--Dissmiss progress dialog
                        pb.dismiss();
//---Try catch block and your other parsing

                        try {


                            JSONObject jobresponse=response.getJSONObject("response");
                            String status=jobresponse.getString("status");
                            String message=jobresponse.getString("message");

                            if(status.equals("1"))
                            {
                                listf=new ArrayList<HashMap<String, String>>();
                                JSONArray fetchecity=jobresponse.getJSONArray("view_owndetail");
                                for(int i=0;i<fetchecity.length();i++)
                                {
                                    JSONObject jobdata= fetchecity.getJSONObject(i);
                                    String add_id=jobdata.getString("add_id");
                                    String add_title= jobdata.getString("add_title");
                                    String add_address= jobdata.getString("add_address");
                                    String add_image= jobdata.getString("add_image");
                                    String owner_name= jobdata.getString("owner_name");
                                    String owner_mobile= jobdata.getString("owner_mobile");
                                    String add_latt= jobdata.getString("add_latt");
                                    String add_long= jobdata.getString("add_long");
                                    String add_category_id= jobdata.getString("add_category_id");
                                    String add_city_id= jobdata.getString("add_city_id");


                                    HashMap<String,String>map=new HashMap<String,String>();

                                    map.put(SessionForm.KEY_add_id,add_id);
                                    map.put(SessionForm.KEY_add_title,add_title);
                                    map.put(SessionForm.KEY_add_address,add_address);
                                    map.put(SessionForm.KEY_owner_name,owner_name);
                                    map.put(SessionForm.KEY_add_image,add_image);
                                    map.put(SessionForm.KEY_owner_mobile,owner_mobile);
                                    map.put(SessionForm.KEY_add_latt,add_latt);
                                    map.put(SessionForm.KEY_add_long,add_long);
                                    map.put(SessionForm.KEY_add_category_id,add_category_id);
                                    map.put(SessionForm.KEY_add_city_id,add_city_id);

                                    listf.add(map);
                                }


                                Adapter_Myadd adcity=new Adapter_Myadd(Activity_MyAdd.this,listf);
                                listupdatepost.setAdapter(adcity);





                            }
                            else
                            {
                                customalert(message,Activity_MyAdd.this);
                            }


//
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            customalert("Something went wrong please check",Activity_MyAdd.this);



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


    public void customalert(String Msg, Activity act) {

        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.customlert);
        TextView tvmsg = (TextView) dialog.findViewById(R.id.txtmsg);
        tvmsg.setText(Msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btnok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}