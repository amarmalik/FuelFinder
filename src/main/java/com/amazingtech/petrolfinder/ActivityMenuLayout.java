package com.amazingtech.petrolfinder;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityMenuLayout extends FragmentActivity {

    ImageView imgmenu;
    FrameLayout frameLayout;
    DrawerLayout drawer_menu__holder;
    LinearLayout llleftslidemenu, ll_select_city;

    TextView txthome, txtfind, txtlogout, txtselectcity, txtmypost, txtcontact, txtnearme, tvselectcity, txtpostadd, txtabout;
    HashMap<String, Integer> Hash_file_maps;
    ArrayList<HashMap<String, String>> datalist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_layout);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        imgmenu = (ImageView) findViewById(R.id.imgmenu);
        drawer_menu__holder = (DrawerLayout) findViewById(R.id.drawer_menu__holder);
        llleftslidemenu = (LinearLayout) findViewById(R.id.llleftslidemenu);
        ll_select_city = (LinearLayout) findViewById(R.id.ll_select_city);
        txtabout = (TextView) findViewById(R.id.txtabout);
        txtcontact = (TextView) findViewById(R.id.txtcontact);
        txtfind = (TextView) findViewById(R.id.txtfind);
        txthome = (TextView) findViewById(R.id.txthome);
        txtlogout = (TextView) findViewById(R.id.txtlogout);


        txtmypost = (TextView) findViewById(R.id.txtmypost);
        txtnearme = (TextView) findViewById(R.id.txtnearme);
        txtselectcity = (TextView) findViewById(R.id.txtselectcity);
        tvselectcity = (TextView) findViewById(R.id.tvselectcity);
        txtpostadd = (TextView) findViewById(R.id.txtpostadd);
        ll_select_city = (LinearLayout) findViewById(R.id.ll_select_city);
        String cityname = SessionForm.GetSharedPreferences(SessionForm.Key_city_name, ActivityMenuLayout.this);
        String areaname = SessionForm.GetSharedPreferences(SessionForm.Key_area_name, ActivityMenuLayout.this);
        drawer_menu__holder = (DrawerLayout) findViewById(R.id.drawer_menu__holder);
        drawer_menu__holder.setScrimColor(Color.parseColor("#80000000"));
        tvselectcity.setText(areaname + " " + cityname);
        Fragment Frag = new FragmentMainHome();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, Frag).commit();
        drawer_menu__holder.closeDrawer(llleftslidemenu);
        txtfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMenuLayout.this, Activity_CityChoice.class);
                startActivity(intent);
            }
        });

    /*    mAuthListner=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ActivityMenuLayout.this, Activity_Login.class));
                   // FirebaseAuth.getInstance().signOut();
                }
            }
        };
*/
       txtlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionForm.SetSharedPreferences(SessionForm.KEY_loginstatus, "false", ActivityMenuLayout.this);
                Intent i = new Intent(ActivityMenuLayout.this, Activity_Login.class);
                startActivity(i);

            }
        });


        ll_select_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ActivityMenuLayout.this, Activity_CityChoice.class);
                startActivity(in);
            }
        });
        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer_menu__holder.isDrawerOpen(llleftslidemenu)) {
                    drawer_menu__holder.closeDrawer(llleftslidemenu);

                } else {
                    drawer_menu__holder.openDrawer(llleftslidemenu);
                }
            }
        });

        txtpostadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMenuLayout.this, Activity_AddPost.class);
                startActivity(intent);
            }
        });
        txtmypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionForm.GetSharedPreferences(SessionForm.KEY_loginstatus, ActivityMenuLayout.this).equals("true")) {
                    Intent in = new Intent(ActivityMenuLayout.this, Activity_MyAdd.class);
                    startActivity(in);
                } else {
                    //Toast.makeText(Menu_Holder.this, "Please login first...", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(ActivityMenuLayout.this, Activity_Login.class);
                    startActivity(in);
                }
            }
        });
        txthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvselectcity.setText("Select City");
                changefragment(new FragmentMainHome());
            }
        });
    }

    public void changefragment(Fragment fragment) {
        //--change fetched fragment to frame layout

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).addToBackStack(null).commit();

        drawer_menu__holder.closeDrawer(llleftslidemenu);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            changefragment(new FragmentMainHome());
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            //changeFragment(new Home());
            finish();
        } else {
            finish();
        }
    }

   /* private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });

    }


    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            txtlogout.setVisibility(View.VISIBLE);

        } else {

            txtlogout.setVisibility(View.GONE);

        }

    }
    private void resolveSignInError() {
       *//* if (connectionResult.hasResolution()) {
            try {
                intentInProgress = true;
                connectionResult.startResolutionForResult(this, RC_SIGN_IN);
            }
            catch (IntentSender.SendIntentException sie) {
                intentInProgress = false;
                mGoogleApiClient.connect();
            }
        }*//*
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

    *//*public void onConnected(@Nullable Bundle bundle) {
        signedInUser = false;
        if (temp == true) {
            type = "2";
            Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

            Intent i = new Intent(RegisterSkipActivity.this, MainActivity.class);
            i.putExtra("Login", type);
            startActivity(i);
            temp = false;
        }
    }*//*


    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        // Log.d(TAG, "onConnectionFailed:" + connectionResult);
      *//*  if (!connectionResult.hasResolution()) {
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
        }*//*

        if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = result.getResolution();
            if (mSignInProgress == STATE_SIGNING_IN) {
                resolveSignInError();
            }
        }
        // Will implement shortly
        signOut();
    }


    @Override
    public void onClick(View view) {

    }*/
}