package com.amazingtech.petrolfinder;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class Activity_Login extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{
    Toolbar toolbar;
    EditText etmail, etpassword;
    TextInputLayout inputLayoutMail, inputLayoutPassword;
    TextView tvlink_signup;
    Button btn_signin;
    private SignInButton google_btn;
    String strmail_signin, strpassword_signin;

    private static final String TAG = Activity_Login.class.getSimpleName();
    private static final int RC_SIGN_IN = 0;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private boolean intentInProgress;
    private boolean signedInUser;
    private static final int SIGNED_IN = 0;
    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;
    private ConnectionResult connectionResult;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    // google login
    /* FirebaseAuth mAuth;
    private static int RC_SIGN_IN = 2;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }*/

    Button facebook_Button;

    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputLayoutMail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        etmail = (EditText) findViewById(R.id.etmail);
        etpassword = (EditText) findViewById(R.id.etpassword);
        tvlink_signup = (TextView) findViewById(R.id.tvlink_signup);
        btn_signin = (Button) findViewById(R.id.btn_signin);

    // google login
       google_btn = (SignInButton) findViewById(R.id.google_btn);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);

        google_btn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_Login.this,ActivityMenuLayout.class);
                startActivity(intent);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        google_btn.setSize(SignInButton.SIZE_STANDARD);
        google_btn.setScopes(gso.getScopeArray());
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        resolveSignInError();
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
          //  String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();


            Log.e(TAG, "Name: " + personName + ", email: " + email);
                  //  + ", Image: " + personPhotoUrl);

            txtName.setText(personName);
            txtEmail.setText(email);
            SessionForm.SetSharedPreferences(SessionForm.KEY_personName,personName,Activity_Login.this);
            SessionForm.SetSharedPreferences(SessionForm.KEY_personEmail,email,Activity_Login.this);
          //  SessionForm.SetSharedPreferences(SessionForm.KEY_googlesign_user_image,personPhotoUrl,Activity_Login.this);
        // profile image
         /*  Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);
*/
          /*  Intent i = new Intent(Activity_Login.this, Activity_Signup.class);
            startActivity(i);
            finish();
*/
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

// facebook

// facebook

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.google_btn:
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_revoke_access:
                revokeAccess();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
        /*else if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                signedInUser = false;
            }

            intentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }*/
        else if (resultCode == RESULT_OK) {
            mSignInProgress = STATE_SIGNING_IN;
        } else {
            mSignInProgress = SIGNED_IN;
        }

        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void resolveSignInError() {
       /* if (connectionResult.hasResolution()) {
            try {
                intentInProgress = true;
                connectionResult.startResolutionForResult(this, RC_SIGN_IN);
            }
            catch (IntentSender.SendIntentException sie) {
                intentInProgress = false;
                mGoogleApiClient.connect();
            }
        }*/
        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                mSignInProgress = STATE_SIGNING_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // You have a play services error -- inform the user
        }
    }

    /*public void onConnected(@Nullable Bundle bundle) {
        signedInUser = false;
        if (temp == true) {
            type = "2";
            Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

            Intent i = new Intent(RegisterSkipActivity.this, MainActivity.class);
            i.putExtra("Login", type);
            startActivity(i);
            temp = false;
        }
    }*/


    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        // Log.d(TAG, "onConnectionFailed:" + connectionResult);
      /*  if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!intentInProgress) {
            // Store the ConnectionResult for later usage

          connectionResult = this.connectionResult;

            if (signedInUser) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }*/

        if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = result.getResolution();
            if (mSignInProgress == STATE_SIGNING_IN) {
                resolveSignInError();
            }
        }
        // Will implement shortly
        signOut();
    }



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            google_btn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            google_btn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }





        etmail.addTextChangedListener(new Activity_Login.MyTextWatcher(etmail));
        etpassword.addTextChangedListener(new Activity_Login.MyTextWatcher(etpassword));

        tvlink_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Activity_Login.this, Activity_Signup.class);
                startActivity(in);
            }
        });

        /*google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });*/
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();

                strmail_signin = etmail.getText().toString();
                strpassword_signin = etpassword.getText().toString();

                if (strmail_signin.equals("")) {
                    Toast.makeText(Activity_Login.this, "Please enter valid email id", Toast.LENGTH_LONG).show();
                } else if (strpassword_signin.equals("")) {
                    Toast.makeText(Activity_Login.this, "please enter valid password", Toast.LENGTH_LONG).show();
                } else {
                    makeJsonObjReq2();

                }
            }
        });
    }


    private void submitForm() {


        if (!validateMail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
    }


    private boolean validateMail() {
        String email = etmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutMail.setError(getString(R.string.err_msg_email));
            requestFocus(etmail);
            return false;
        } else {
            inputLayoutMail.setErrorEnabled(false);
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

                case R.id.etmail:
                    validateMail();
                    break;
                case R.id.etpassword:
                    validatePassword();
                    break;
            }
        }
    }



    private void makeJsonObjReq2() {
        //---For Display Progressbar
        final ProgressDialog pb = new ProgressDialog(Activity_Login.this);
        pb.setMessage("Please wait..");
        pb.show();
//--Replace your url
        final String urlweb = Url.GetUrl("http://ezeonsoft.in/fuelfinder/login_users.php?" + "email=" + strmail_signin + "&password=" + strpassword_signin);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlweb, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject result) {
                        Log.d("ressult", result.toString());
//--Dissmiss progress dialog
                        if(pb!=null)
                        {
                            pb.dismiss();
                        }
//---Try catch block and your other parsing

                        try {
//

                            JSONObject jobresponse = result.getJSONObject("response");

                            String status = jobresponse.getString("status");
                            String message = jobresponse.getString("message");

                            if (status.equals("1")) {
                                String email = jobresponse.getString("email");
                                String password = jobresponse.getString("password");
                                String mobile = jobresponse.getString("mobile");
                                String name = jobresponse.getString("name");
                                String user_id = jobresponse.getString("user_id");
                                String user_status = jobresponse.getString("user_status");
                                String device_token = jobresponse.getString("device_token");

                                SessionForm.SetSharedPreferences(SessionForm.KEY_email, email, Activity_Login.this);
                                SessionForm.SetSharedPreferences(SessionForm.KEY_name, name, Activity_Login.this);
                                SessionForm.SetSharedPreferences(SessionForm.KEY_mobile, mobile, Activity_Login.this);
                                SessionForm.SetSharedPreferences(SessionForm.Key_password, password, Activity_Login.this);
                                SessionForm.SetSharedPreferences(SessionForm.KEY_user_id, user_id, Activity_Login.this);
                                SessionForm.SetSharedPreferences(SessionForm.KEY_user_status, user_status, Activity_Login.this);
                                SessionForm.SetSharedPreferences(SessionForm.KEY_device_token, device_token, Activity_Login.this);


                                SessionForm.SetSharedPreferences(SessionForm.KEY_loginstatus, "true", Activity_Login.this);


                                Intent i = new Intent(Activity_Login.this, ActivityMenuLayout.class);
                                startActivity(i);
                                finish();
                            } else {
                                customalert(message, Activity_Login.this);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            customalert("Something went wrong please check", Activity_Login.this);
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
    public void customalert(String Msg, Activity act) {
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
// to checkwether internet coonection is on or not..
    /*
    public boolean CheckInternet(Context con) {
        boolean flag = false;

        if (isNetworkAvailable()) {
            flag = true;

        } else {

	 			*//*AlertDialogMsg obAlertDialogMsg = new AlertDialogMsg(_context,
	 					"Check Internet");*//*
            //obAlertDialogMsg.show();

            flag = false;
        }
        return flag;

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }*/

    }
