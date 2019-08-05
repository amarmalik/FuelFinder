package com.amazingtech.petrolfinder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class Activity_CityChoice extends AppCompatActivity {
    LinearLayout ll_choosecity;
        Spinner spinnercity,spinnerarea;
    ImageView iv_choose_back;
    ArrayList<HashMap<String, String>> listf;
    ArrayList<HashMap<String, String>> listarea;
    String selectedcityname="";
    String selectedcityid="";
    String selectedareaid="";
    String selectedareaname="";
    Button btnserchcity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__city_choice);
        ll_choosecity = (LinearLayout) findViewById(R.id.ll_choosecity);
        iv_choose_back = (ImageView) findViewById(R.id.iv_choose_back);
        spinnercity = (Spinner) findViewById(R.id.spinnercity);
        spinnerarea = (Spinner) findViewById(R.id.spinnerarea);
        btnserchcity = (Button)findViewById(R.id.btnserchcity);
        listf = new ArrayList<HashMap<String, String>>();
        listarea = new ArrayList<HashMap<String, String>>();
        btnserchcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_CityChoice.this,ActivityMenuLayout.class);
                startActivity(intent);
            }
        });

        iv_choose_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_CityChoice.this, ActivityMenuLayout.class);
                startActivity(i);
                finish();
            }
        });


        CitySpinnermakeJsonObjReq2();


    }

    private void CitySpinnermakeJsonObjReq2() {
        //---For Display Progressbar
        final ProgressDialog pb = new ProgressDialog(Activity_CityChoice.this);

        pb.setMessage("Please wait..");
        pb.show();
//--Replace your url
        final String urlweb = Url.GetUrl("http://ezeonsoft.in/fuelfinder/view_city.php");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlweb, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
//--Dissmiss progress dialog
                        if(pb!=null)
                        {
                            pb.dismiss();
                        }
//---Try catch block and your other parsing

                        try {

                            JSONObject jobresponse = response.getJSONObject("response");
                            String status = jobresponse.getString("status");
                            String message = jobresponse.getString("message");
                          //  listf = new ArrayList<HashMap<String, String>>();

                            if (status.equals("1")) {
                                JSONArray fetchecity = jobresponse.getJSONArray("fetch_city");
                                for (int i = 0; i <fetchecity.length(); i++) {
                                    JSONObject jobdata = fetchecity.getJSONObject(i);
                                    String city_id = jobdata.getString("city_id");
                                    String city_name = jobdata.getString("city_name");


                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(SessionForm.Key_city_id, city_id);
                                    map.put(SessionForm.Key_city_name, city_name);
                                    listf.add(map);
                                }
                                Adapter_Search adcity = new Adapter_Search(Activity_CityChoice.this, listf);
                                spinnercity.setAdapter(adcity);
                                spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        HashMap<String,String>cmapa =listf.get(position);
                                        selectedcityname =cmapa.get(SessionForm.Key_city_name);
                                        selectedcityid=cmapa.get(SessionForm.Key_city_id);
                                        SessionForm.SetSharedPreferences(SessionForm.Key_city_name, selectedcityname,Activity_CityChoice.this);
                                        SessionForm.SetSharedPreferences(SessionForm.Key_city_id,selectedcityid,Activity_CityChoice.this);
                                        AreaSpinnermakeJsonObjReq2();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            }




//
                        } catch (Exception ex) {

                            customalert("Something went wrong please check", Activity_CityChoice.this);


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

    private void AreaSpinnermakeJsonObjReq2() {
        //---For Display Progressbar
        final ProgressDialog pb = new ProgressDialog(Activity_CityChoice.this);

        pb.setMessage("Please wait..");
        pb.show();
//--Replace your url
        final String urlweb = Url.GetUrl("http://ezeonsoft.in/fuelfinder/view_area.php?"+"city_id="+selectedcityid);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlweb, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
//--Dissmiss progress dialog
                        if(pb!=null)
                        {
                            pb.dismiss();
                        }
//---Try catch block and your other parsing

                        try {


                            JSONObject jobresponse = response.getJSONObject("response");
                            String status = jobresponse.getString("status");
                            String message = jobresponse.getString("message");
                            listf = new ArrayList<HashMap<String, String>>();

                            if (status.equals("1")) {
                                JSONArray fetchecity = jobresponse.getJSONArray("fetch_area");
                                for (int i = 0; i < fetchecity.length(); i++) {
                                    JSONObject jobdata = fetchecity.getJSONObject(i);
                                    String city_id = jobdata.getString("city_id");
                                    String area_name = jobdata.getString("area_name");
                                    String area_id = jobdata.getString("area_id");


                                    HashMap<String, String> map = new HashMap<String, String>();

                                    map.put(SessionForm.Key_city_id, city_id);
                                    map.put(SessionForm.Key_area_id, area_id);
                                    map.put(SessionForm.Key_area_name, area_name);
                                    listarea.add(map);
                                }


                                Adapter_SearchArea adcity = new Adapter_SearchArea(Activity_CityChoice.this, listarea);
                                spinnerarea.setAdapter(adcity);
                                spinnerarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        HashMap<String, String> cmapa = listarea.get(position);
                                        selectedcityid = cmapa.get(SessionForm.Key_city_id);
                                        selectedareaname = cmapa.get(SessionForm.Key_area_name);
                                        selectedareaid = cmapa.get(SessionForm.Key_area_id);
                                        SessionForm.SetSharedPreferences(SessionForm.Key_city_id, selectedcityid, Activity_CityChoice.this);
                                        SessionForm.SetSharedPreferences(SessionForm.Key_area_id, selectedareaid, Activity_CityChoice.this);
                                        SessionForm.SetSharedPreferences(SessionForm.Key_area_name, selectedareaname, Activity_CityChoice.this);


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            else if (status.equals("0")) {
                                listarea = new ArrayList<HashMap<String, String>>();
                                HashMap<String,String>cmap=new HashMap<String, String>();
                                cmap.put(SessionForm.Key_area_name,"There is no Area in the list");
                                listarea.add(cmap);
                                Adapter_SearchArea adcity = new Adapter_SearchArea(Activity_CityChoice.this, listarea);
                                spinnerarea.setAdapter(adcity);

                            }

                            else {
                                customalert(message,Activity_CityChoice.this);

                            }


//
                        } catch (Exception ex) {
                            ex.printStackTrace();


                        }


//----
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Userchoice","Error:" +error.getMessage());
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
        TextView txtmsg = (TextView) dialog.findViewById(R.id.txtmsg);
        txtmsg.setText(Msg);

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
