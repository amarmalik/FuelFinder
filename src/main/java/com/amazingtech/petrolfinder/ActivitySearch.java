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

public class ActivitySearch extends AppCompatActivity {
    ListView lv_search_list;
    ImageView iv_city_back;
    ArrayList<HashMap<String,String>> listf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        iv_city_back=(ImageView)findViewById(R.id.iv_city_back);

        lv_search_list=(ListView)findViewById(R.id.lv_search_list);
        listf=new ArrayList<HashMap<String,String>>();
        iv_city_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ActivitySearch.this,ActivityMenuLayout.class);
                startActivity(i);
                finish();
            }
        });


        makeJsonObjReq2();


    }
    private void makeJsonObjReq2() {
        //---For Display Progressbar
        final ProgressDialog pb = new ProgressDialog(ActivitySearch.this);

        pb.setMessage("Please wait..");
        pb.show();
//--Replace your url
        final String urlweb = Url.GetUrl("http://ezeonsoft.in/ezeadd/view_city.php");
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
                                JSONArray fetchecity=jobresponse.getJSONArray("fetch_city");
                                for(int i=0;i<fetchecity.length();i++)
                                {
                                    JSONObject jobdata= fetchecity.getJSONObject(i);
                                    String city_id=jobdata.getString("city_id");
                                    String city_name= jobdata.getString("city_name");


                                    HashMap<String,String>map=new HashMap<String,String>();

                                    map.put(SessionForm.Key_city_id,city_id);
                                    map.put(SessionForm.Key_city_name,city_name);
                                    listf.add(map);
                                }


                                Adapter_Search adcity=new Adapter_Search(ActivitySearch.this,listf);
                                lv_search_list.setAdapter(adcity);





                            }
                            else
                            {
                                customalert(message,ActivitySearch.this);
                            }


//
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            customalert("Something went wrong please check",ActivitySearch.this);



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
        TextView tvsearch = (TextView) dialog.findViewById(R.id.tvsearch);
        tvsearch.setText(Msg);

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

