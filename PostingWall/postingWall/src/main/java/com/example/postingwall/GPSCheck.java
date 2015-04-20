package com.example.postingwall;

/**
 * Created by Danish Goel on 01-Apr-15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public class GPSCheck extends Activity
{
    /*---------------------------------------------------------*/
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static String PROPERTY_REG_ID = "registration_id";
    private static String PROPERTY_APP_VERSION = "appVersion";
    private final static String TAG = "LaunchActivity";
    protected String SENDER_ID = "212535033582";
    private GoogleCloudMessaging gcm =null;
    public static String regid = null;
    private Context context= GPSCheck.this;
    final String PREFS = "gcm";
    /*---------------------------------------------------------*/
    boolean internet=false;
    boolean gps=false;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
        context = getApplicationContext();
        if (checkPlayServices())
        {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            Log.d("reg1",regid);
            if (regid.isEmpty())
            {
                registerInBackground();
                Log.d("reg2",regid);
            }
            else
            {
                Log.d(TAG, "No valid Google Play Services APK found.");
            }
        }
        Log.d("register",regid);
        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        checkPlayServices();
        ConnectivityManager conMgr  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        if(info != null && info.isConnected())
        {
            internet=true;
            Log.d("net", "1net on");
        }
        else
        {
            Log.d("net", "net off");
            buildAlertMessageNoInternet();
        }
        if(internet==true)
        {
            Log.d("net", "2net on");
            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) || !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) )
            {
                Log.d("net", "gps off");
                buildAlertMessageNoGps();
            }
            else
            {
                Log.d("net", "gps on");
                gps=true;
                Intent i;
                i=new Intent(GPSCheck.this,getLocation.class);
                startActivity(i);
            }
        }


    }


    private void buildAlertMessageNoInternet()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Internet connection seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        dialog.cancel();
                        new AlertDialog.Builder(GPSCheck.this)
                                .setTitle("Warning")
                                .setMessage("This App can't be used without Internet.\nClick OK to close.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    private void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS or Network Location seems to be disabled, do you want to enable it?\n\nNote: Allowing both GPS and Network Location (high accuracy) is suggested.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        dialog.cancel();
                        new AlertDialog.Builder(GPSCheck.this)
                                .setTitle("Warning")
                                .setMessage("This App can't be used without GPS.\nClick OK to close.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    /*---------------------------Push Notification---------------------*/

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d(TAG, "This device is not supported - Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context)
    {
        SharedPreferences sett = getSharedPreferences(PREFS, 0);
        String registrationId = sett.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.d(TAG, "Registration ID not found.");
            return "";
        }
//        int registeredVersion = sett.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//        int currentVersion = getAppVersion(context);
//        if (registeredVersion != currentVersion) {
//            Log.d(TAG, "App version changed.");
//            return "";
//        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context)
    {
        return getSharedPreferences(GPSCheck.class.getSimpleName(),Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    private void registerInBackground()
    {
        new AsyncTask() {
            protected Object doInBackground(Object... params)
            {
                String msg = "";
                try
                {
                    if (gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);               Log.d(TAG, "########################################");
                    Log.d(TAG, "Current Device's Registration ID is: "+msg);
                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();
                }
                return null;
            }     protected void onPostExecute(Object result)
            { //to do here };
            }}.execute(null, null, null);
//        new RegisterTask().execute();
    }



    public class RegisterTask extends AsyncTask<Object,Object,Object>
    {

        protected Object doInBackground(Object... params)
        {
            String msg = "";
            try
            {
                if (gcm == null)
                {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);               Log.d(TAG, "########################################");
                Log.d(TAG, "Current Device's Registration ID is: "+msg);
            }
            catch (IOException ex)
            {
                msg = "Error :" + ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);
        }

    }



/*---------------------------Push Notification---------------------*/
}
