package com.amazingtech.petrolfinder;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Activity_Updatepost extends AppCompatActivity {
    ImageView iv_browse_image, iv_post_back;
    String strname, straddress, strownername, strmobile, stremail, strprice,strlatt,strlong;
    EditText et_servicename,et_address,et_owner,et_mobile,et_lattitude,et_longitude;
    Button btnbrowse, btn_postupdate;
    Spinner spinner_category;
    Spinner spinner_city;
    Spinner spinner_area;
    String Selectted_id = "";
    String selecttedcity_id = "";
    String Selecttedarea_id= "";
    String Selecttedarea_name= "";
    String Selecttedcity_name= "";
    ArrayList<HashMap<String, String>> arrcity;
    ArrayList<HashMap<String, String>> arryarea;
    ArrayList<HashMap<String, String>> arrcate;
    private Uri fileUri;

    //---Permission
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    //---selected image location holding
    String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__updatepost);
        iv_browse_image=(ImageView)findViewById(R.id.iv_browse_image);
        iv_post_back=(ImageView)findViewById(R.id.iv_post_back);
        et_lattitude=(EditText)findViewById(R.id.et_lattitude);
        et_longitude=(EditText)findViewById(R.id.et_longitude);
        et_owner=(EditText)findViewById(R.id.et_owner);
        et_mobile=(EditText)findViewById(R.id.et_mobile);
        et_address=(EditText)findViewById(R.id.et_address);
        btnbrowse=(Button) findViewById(R.id.btnbrowse);
        btn_postupdate=(Button)findViewById(R.id.btn_postupdate);
        spinner_category=(Spinner) findViewById(R.id.spinner_category);
        spinner_city=(Spinner) findViewById(R.id.spinner_city);
        spinner_area=(Spinner) findViewById(R.id.spinner_area);

        et_servicename.setText(SessionForm.selectedmap.get(SessionForm.KEY_add_title));
        et_address.setText(SessionForm.selectedmap.get(SessionForm.KEY_add_address));
        et_owner.setText(SessionForm.selectedmap.get(SessionForm.KEY_owner_name));
        et_mobile.setText(SessionForm.selectedmap.get(SessionForm.KEY_owner_mobile));
        et_lattitude.setText(SessionForm.selectedmap.get(SessionForm.KEY_add_latt));
        et_longitude.setText(SessionForm.selectedmap.get(SessionForm.KEY_add_long));
        // iv_browse_image.setImag(SessionForm.selectedmap.get(SessionForm.KEY_add_image));
        arrcity = new ArrayList<HashMap<String, String>>();
        arryarea = new ArrayList<HashMap<String, String>>();
        arrcate = new ArrayList<HashMap<String, String>>();

        makeJsonObjReq2();
        makeJsonObjReq3();


        iv_post_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Updatepost.this, ActivityMenuLayout.class);
                startActivity(i);
                finish();
            }
        });
        btn_postupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strmobile = et_mobile.getText().toString();
                strname = et_servicename.getText().toString();
                strownername = et_owner.getText().toString();
                straddress = et_address.getText().toString();
                strlatt = et_lattitude.getText().toString();
                strlong = et_longitude.getText().toString();



                if (strmobile.equals(""))

                {
                    Toast.makeText(Activity_Updatepost.this, "Please enter your mobile", Toast.LENGTH_LONG).show();

                } else if (strmobile.length() != 10)

                {
                    Toast.makeText(Activity_Updatepost.this, "Please enter your valid no", Toast.LENGTH_LONG).show();

                } else if (strownername.equals(""))

                {
                    Toast.makeText(Activity_Updatepost.this, "Please enter owner name", Toast.LENGTH_LONG).show();

                } else if (straddress.equals(""))

                {
                    Toast.makeText(Activity_Updatepost.this, "Please enter fuel address", Toast.LENGTH_LONG).show();

                } else if (strlong.equals(""))

                {
                    Toast.makeText(Activity_Updatepost.this, "Please enter longitude", Toast.LENGTH_LONG).show();

                }
                if (strlatt.equals(""))

                {
                    Toast.makeText(Activity_Updatepost.this, "Please enter lattitude", Toast.LENGTH_LONG).show();

                } else

                {
                    MakeJsonObject();


                }

            }
        });


        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyStoragePermissions(Activity_Updatepost.this) == true) {
                    selectMedia();
                } else {
                    ActivityCompat.requestPermissions(
                            Activity_Updatepost.this,
                            PERMISSIONS_STORAGE,
                            1
                    );
                }
            }
        });

        if (verifyStoragePermissions(Activity_Updatepost.this) == true) {

        } else {
            ActivityCompat.requestPermissions(
                    Activity_Updatepost.this,
                    PERMISSIONS_STORAGE,
                    1
            );
        }
    }



    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        boolean status = false;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            status = false;
            // We don't have permission so prompt the user

        } else {
            status = true;
        }
        return status;
    }


    private void selectMedia() {
        final Dialog dialog = new Dialog(Activity_Updatepost.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity__custom__dilog__add);
        Button btnGallery =(Button)dialog.findViewById(R.id.btngallery);
        Button btnCamera =(Button)dialog.findViewById(R.id.btncamera);
        final Button btncancel =(Button)dialog.findViewById(R.id.btncancel);

        btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, 1);


                dialog.dismiss();

            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                dialog.dismiss();

            }
        });

        dialog.show();

    }
//---copy both function and call only below function at camera capture

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "mytrip");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //--Your code start

        if (resultCode == RESULT_OK) {


            //---for camera fetching image
            if (requestCode == 1) {
                BitmapFactory.Options options = new BitmapFactory.Options();

                try {
                    options.inSampleSize = 8;

                    //---get current path of captured image
                    mCurrentPhotoPath= fileUri.getPath();
                    //---convert file location uri to Bitmap
                    Bitmap bmap = (BitmapFactory.decodeFile(mCurrentPhotoPath));

                    //---Compress file to required size
                    Bitmap resultBitmap = Bitmap.createScaledBitmap(bmap, 500, 500, true);

                    iv_browse_image.setImageBitmap(resultBitmap);

                    bmap.recycle();
                    bmap=null;

                    mCurrentPhotoPath=	saveToInternalStorage(resultBitmap,"userpic.jpg");

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }


            }

//--for gallery fetching image
            else if (requestCode == 2) {




                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };



                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                Bitmap     bitmap = (BitmapFactory.decodeFile(picturePath));
                Bitmap resultBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
                bitmap.recycle();
                bitmap=null;
                iv_browse_image.setImageBitmap(resultBitmap);
                try {
                    mCurrentPhotoPath=	saveToInternalStorage(resultBitmap,"userpic.jpg");

                } catch (IOException e) {
                }
                // ImagePath=picturePath;

            }




        }
//---End your code

    }



    private String saveToInternalStorage(Bitmap bitmapImage,String filename) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=null;

        mypath  =new File(directory,filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath()+"/"+filename;
    }
    public void MakeJsonObject()
    {

        final ProgressDialog pd=new ProgressDialog(Activity_Updatepost.this);
        pd.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("add_category_id",Selectted_id);
        params.put("add_address",straddress);
        params.put("add_title", strname);
        params.put("add_id",SessionForm.selectedmap.get(SessionForm.KEY_add_id));
        params.put("owner_name",strownername);
        params.put("owner_mobile",strmobile);
        params.put("add_latt",strlatt);
        params.put("add_long",strlong);
        params.put("area_id",Selecttedarea_id);
        params.put("city_id",selecttedcity_id);
        params.put("user_id",SessionForm.GetSharedPreferences(SessionForm.KEY_user_id,Activity_Updatepost.this));

        File f = null;

        try{
            if(mCurrentPhotoPath!=null && !mCurrentPhotoPath.equals(""))
                f 			 = new File(mCurrentPhotoPath);


        }catch(Exception ev){

        }

//---copy this code

        Multipart_Request mr = new Multipart_Request("http://ezeonsoft.in/fuelfinder/update_user_detail.php?",new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response) {
                Log.d("response", response);



                try {
                    JSONObject jobresult=new JSONObject(response);
                    JSONObject jobresponse = jobresult.getJSONObject("response");
                    String status=jobresponse.getString("status");
                    String message=jobresponse.getString("message");

                    if(status.equals("1"))
                    {


                        Toast.makeText(Activity_Updatepost.this, message, Toast.LENGTH_SHORT).show();


                    }
                    else
                    {
                        Toast.makeText(Activity_Updatepost.this, message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {

                }



                pd.dismiss();

            }

        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.e("Volley Request Error", error.getLocalizedMessage());
                pd.dismiss();
            }

        }, f, params);

        Volley.newRequestQueue(this).add(mr);

    }
    private void makeJsonObjReq2() {
        //---For Display Progressbar
        final ProgressDialog pb = new ProgressDialog(Activity_Updatepost.this);
        pb.setMessage("Please wait..");
        pb.show();
//--Replace your url
        final String urlweb = Url.GetUrl("http://ezeonsoft.in/fuelfinder/fetch_category.php?");
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
                                arrcate=new ArrayList<HashMap<String, String>>();
                                JSONArray fetchecatogery=jobresponse.getJSONArray("fetch_category");
                                for(int i=0;i<fetchecatogery.length();i++)
                                {
                                    JSONObject jobdata= fetchecatogery  .getJSONObject(i);
                                    String category_id=jobdata.getString("category_id");
                                    String category_name=jobdata.getString("category_name");



                                    HashMap<String,String>map=new HashMap<String,String>();


                                    map.put(SessionForm.Key_category_id, category_id);
                                    map.put(SessionForm.Key_category_name, category_name);

                                    arrcate.add(map);

                                }


                                Adapter_Add_project adspuser=new Adapter_Add_project(Activity_Updatepost.this,arrcate);
                                spinner_category.setAdapter(adspuser);

                                spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        HashMap<String, String> cmap = arrcate.get(position);
                                        Selectted_id = cmap.get(SessionForm.Key_category_id);


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });




                            }
                            else
                            {
                                customalert(message,Activity_Updatepost.this);
                            }


//
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            customalert("Something went wrong please check",Activity_Updatepost.this);



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

    private void makeJsonObjReq3() {
        //---For Display Progressbar
        final   ProgressDialog pb = new ProgressDialog(Activity_Updatepost.this);
        pb.setMessage("Please wait..");
        pb.show();
//--Replace your url
        final String urlweb = Url.GetUrl("http://ezeonsoft.in/fuelfinder/view_city.php?");
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
                                arrcity=new ArrayList<HashMap<String, String>>();
                                JSONArray fetch_city=jobresponse.getJSONArray("fetch_city");
                                for(int i=0;i<fetch_city.length();i++)
                                {
                                    JSONObject jobdata= fetch_city  .getJSONObject(i);
                                    String city_id=jobdata.getString("city_id");
                                    String city_name=jobdata.getString("city_name");




                                    HashMap<String,String>map=new HashMap<String,String>();


                                    map.put(SessionForm.Key_city_id, city_id);
                                    map.put(SessionForm.Key_city_name, city_name);


                                    arrcity.add(map);

                                }


                                Adapter_Search adspuser=new Adapter_Search(Activity_Updatepost.this,arrcity);
                                spinner_city.setAdapter(adspuser);

                                spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        HashMap<String, String>cmap = arrcity.get(position);
                                        selecttedcity_id =cmap.get(SessionForm.Key_city_id);
                                        Selecttedcity_name = cmap.get(SessionForm.Key_city_name);
                                        SessionForm.SetSharedPreferences(SessionForm.Key_city_name,Selecttedcity_name,Activity_Updatepost.this);
                                        SessionForm.SetSharedPreferences(SessionForm.Key_city_id,selecttedcity_id,Activity_Updatepost.this);
                                        makeJsonObjReq4();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });




                            }
                            else
                            {
                                customalert(message,Activity_Updatepost.this);
                            }


//
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            customalert("Something went wrong please check",Activity_Updatepost.this);



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

    private void makeJsonObjReq4() {
        //---For Display Progressbar
        final   ProgressDialog pb = new ProgressDialog(Activity_Updatepost.this);
        pb.setMessage("Please wait..");
        pb.show();
//--Replace your url
        final String urlweb = Url.GetUrl("http://ezeonsoft.in/fuelfinder/view_area.php?"+"city_id="+selecttedcity_id);
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
                                arryarea=new ArrayList<HashMap<String, String>>();
                                JSONArray fetch_area=jobresponse.getJSONArray("fetch_area");
                                for(int i=0;i<fetch_area.length();i++)
                                {
                                    JSONObject jobdata= fetch_area  .getJSONObject(i);
                                    String city_id=jobdata.getString("city_id");
                                    String area_id=jobdata.getString("area_id");
                                    String area_name=jobdata.getString("area_name");




                                    HashMap<String,String>map=new HashMap<String,String>();


                                    map.put(SessionForm.Key_city_id, city_id);
                                    map.put(SessionForm.Key_area_id, area_id);
                                    map.put(SessionForm.Key_area_name, area_name);


                                    arryarea.add(map);

                                }


                                Adapter_SearchArea adspuser=new Adapter_SearchArea(Activity_Updatepost.this,arryarea);
                                spinner_area.setAdapter(adspuser);

                                spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        HashMap<String, String> cmap = arryarea.get(position);
                                        Selecttedarea_id = cmap.get(SessionForm.Key_area_id);
                                        Selecttedarea_name = cmap.get(SessionForm.Key_area_name);
                                        selecttedcity_id=cmap.get(SessionForm.Key_city_id);
                                        SessionForm.SetSharedPreferences(SessionForm.Key_city_id, selecttedcity_id, Activity_Updatepost.this);
                                        SessionForm.SetSharedPreferences(SessionForm.Key_area_id, Selecttedarea_id, Activity_Updatepost.this);
                                        SessionForm.SetSharedPreferences(SessionForm.Key_area_name, Selecttedarea_name, Activity_Updatepost.this);



                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });




                            }
                            else
                            {
                                customalert(message,Activity_Updatepost.this);
                            }


//
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            customalert("Something went wrong please check",Activity_Updatepost.this);

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
