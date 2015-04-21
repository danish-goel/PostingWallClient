package com.example.postingwall;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.postingwall.classes.Constants;
import com.example.postingwall.classes.User;

public class getLocation extends Activity 
{
	LocationManager manager;
	Criteria criteria;
	ProgressDialog pd;
	boolean gotOnce=false;
    public final static String PREFS_NAME = "login";
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		init();
		initPD();
		pd.show();
		startLocationFetching();
	}

	public void init()
	{
		criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
		criteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
		manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
	}
	
		private void startLocationFetching() 
		{
			
			// TODO Auto-generated method stub
			new Handler().postDelayed(new Runnable() 
			{
				
				@Override
				public void run() 
				{
					// TODO Auto-generated method stub
					manager.requestLocationUpdates(250, 1, criteria, new LocationListener() 
					{
						@Override public void onStatusChanged(String provider, int status, Bundle extras) {}
						@Override public void onProviderEnabled(String provider) {}
						@Override public void onProviderDisabled(String provider) {}
		
						@Override
						public void onLocationChanged(Location location) 
						{
							// TODO Auto-generated method stub
							if(!gotOnce)
							{
								gotOnce=true;

                                Constants.latitude=location.getLatitude();
								Constants.longitude=location.getLongitude();
                                Constants.current_latitude=Constants.latitude;
                                Constants.current_longitude=Constants.longitude;

								Log.d("loc",String.valueOf(Constants.latitude));
								Log.d("loc",String.valueOf(Constants.longitude));

                                SharedPreferences sett = getSharedPreferences(PREFS_NAME, 0);
                                boolean loginStatus=sett.getBoolean("login",false);

                                new BgTask().execute();
                                if(loginStatus)
                                {
                                    Intent fetchHomepage=new Intent(Constants.homepage);
                                    startActivity(fetchHomepage);
                                }
                                else
                                {
                                    Intent login=new Intent(Constants.login);
                                    startActivity(login);
                                    finish();

                                }

								pd.dismiss();
								
							}
							else
								manager.removeUpdates(this);
						}
					}, null);
				}
			}, 300);
		}


    public class BgTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            initPD();
            pd.show();
            SharedPreferences sett = getSharedPreferences(PREFS_NAME, 0);
            String email=sett.getString("useremail","");
            String name=sett.getString("username","");
            Constants.user_email=email;
            Constants.user_name=name;
            Constants.gcm_regid=GPSCheck.regid;
            Log.d("regi", GPSCheck.regid);
//            Constants.user=new User(email,name);
            Constants.user=new User(email,name,GPSCheck.regid);

            SharedPreferences pref = getSharedPreferences("gcm", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("registration_id",GPSCheck.regid);
            editor.commit();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            if(Constants.postService.findUserByEmail(Constants.user_email)!=null)
            {
                Log.d("user","existing user");
            }
            else {
                Constants.postService.addUser(Constants.user);
                Log.d("user","creating user");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            try{pd.setCancelable(true);}catch(Exception e){}
            try{pd.dismiss();}catch(Exception e){}
            super.onPostExecute(result);
        }

    }


    public void initPD()
		 {
				pd=new ProgressDialog(getLocation.this);
				pd.setTitle("Please Wait...");
				pd.setMessage("Fetching Location");
				pd.setIndeterminate(true);
				pd.setCancelable(false);
		 }
		 
}
