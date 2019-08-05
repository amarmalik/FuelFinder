package com.amazingtech.petrolfinder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Activity_Signup extends AppCompatActivity {
    Toolbar toolbar;
    EditText etname, etemail, etpassword, etmobile;
    TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutMobile, inputLayoutPassword;
    TextView tvlink_signup;
    Button btnSignUp;
    String stremail_signup,strmobile_signup,strpassword_signup,strusername_signup;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__signup);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutMobile = (TextInputLayout) findViewById(R.id.input_layout_Mobile);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        etname = (EditText) findViewById(R.id.etname);
        etemail = (EditText) findViewById(R.id.etemail);
        etmobile = (EditText) findViewById(R.id.etmobile);
        etpassword = (EditText) findViewById(R.id.etpassword);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        tvlink_signup = (TextView) findViewById(R.id.tvlink_signup);

        etname.addTextChangedListener(new MyTextWatcher(etname));
        etemail.addTextChangedListener(new MyTextWatcher(etemail));
        etmobile.addTextChangedListener(new MyTextWatcher(etmobile));
        etpassword.addTextChangedListener(new MyTextWatcher(etpassword));

        tvlink_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Activity_Signup.this, Activity_Login.class);
                startActivity(in);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
                strmobile_signup = etmobile.getText().toString();
                stremail_signup = etemail.getText().toString();
                strpassword_signup = etpassword.getText().toString();
                strusername_signup = etname.getText().toString();

                if(strmobile_signup.equals(""))
                {
                    Toast.makeText(Activity_Signup.this,"Please enter your mobile number",Toast.LENGTH_LONG).show();
                }
                else if(stremail_signup.equals(""))
                {
                    Toast.makeText(Activity_Signup.this,"Please enter your email id",Toast.LENGTH_LONG).show();
                }
                else if(strpassword_signup.equals("")){
                    Toast.makeText(Activity_Signup.this,"please enter your password",Toast.LENGTH_LONG).show();
                }

                else if(strusername_signup.equals("")){
                    Toast.makeText(Activity_Signup.this,"please enter your username",Toast.LENGTH_LONG).show();
                }
                else
                {
                    makeJsonObjReq2();

                }
            }
        });
            }


            //  SessionForm.SetSharedPreferences(SessionForm.KEY_googlesign_user_image,personPhotoUrl,Activity_Login.this);
            // profile image
         /*  Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);
*/


    private void makeJsonObjReq2() {
        //---For Display Progressbar
        final ProgressDialog pb=new ProgressDialog(Activity_Signup.this);
        pb.setMessage("Please waits..");
        pb.show();
//--Replace your url
        final String urlweb= Url.GetUrl("http://ezeonsoft.in/fuelfinder/register_add_users.php?" + "name=" + strusername_signup + "&mobile=" + strmobile_signup + "&email=" + stremail_signup    + "&password=" + strpassword_signup);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlweb, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject result) {
                        Log.d("result", result.toString());
//--Dissmiss progress dialog
                        pb.dismiss();
//---Try catch block and your other parsing

                        try
                        {
//

                            JSONObject jobresponse=result.getJSONObject("response");

                            String status=jobresponse.getString("status");
                            String message=jobresponse.getString("message");

                            if(status.equals("1"))
                            {
                                String user_id=jobresponse.getString("user_id");
                                String password=jobresponse.getString("password");

                                {


                                    SessionForm.SetSharedPreferences(SessionForm.Key_password, password, Activity_Signup.this);
                                    SessionForm.SetSharedPreferences(SessionForm.KEY_user_id, user_id, Activity_Signup.this);

                                }


                                SessionForm.SetSharedPreferences(SessionForm.KEY_loginstatus, "true", Activity_Signup.this);

                                Intent i = new Intent(Activity_Signup.this, ActivityMenuLayout.class);
                                startActivity(i);
                                finish();

                            }
                            else
                            {
                                customalert(message,Activity_Signup.this);
                            }

                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            customalert("Something went wrong please check",Activity_Signup.this);

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



    //--end for calling webservice

    public  void customalert(String Msg,Activity act)
    {

        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.customdialog);
        TextView text = (TextView) dialog.findViewById(R.id.txmsg);
        text.setText(Msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btnok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });


        dialog.show();
    }



    private void submitForm() {
        if (!validateName()) {
            return;
        }


        if (!validateEmail()) {
            return;
        }

        if (!validateMobile()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }


    private boolean validateName() {
        if (etname.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(etname);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = etemail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(etemail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateMobile() {
        if (etmobile.getText().toString().trim().isEmpty()) {
            inputLayoutMobile.setError(getString(R.string.err_msg_mobile));
            requestFocus(etmobile);
            return false;
        } else {
            inputLayoutMobile.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (etpassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(etpassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etname:
                    validateName();
                    break;
                case R.id.etmobile:
                    validateName();
                    break;
                case R.id.etemail:
                    validateEmail();
                    break;
                case R.id.etpassword:
                    validatePassword();
                    break;
            }
        }
    }
}

