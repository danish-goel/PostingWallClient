package com.example.postingwall.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.postingwall.R;
import com.example.postingwall.classes.Constants;
import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class Login extends ActionBarActivity implements OnClickListener,ConnectionCallbacks,OnConnectionFailedListener
{
	ListView listView ;
	private static final int RC_SIGN_IN = 0;
	private static final int FB_SIGN_IN = 1;
	private boolean mSignInClicked;
	private Location mCurrentLocation;
	private LocationClient mLocationClient;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private SignInButton btnSignIn;
	private boolean mIntentInProgress;
	private ConnectionResult mConnectionResult;
	private GoogleApiClient mGoogleApiClient;
	private LoginFragment loginFragment;
	private static final String TAG = "FB";
	boolean gps=false;
	LocationManager manager;
	Bundle savedInstanceState;
	Button fb;
	ProgressDialog pd;
//	@Override
//	protected void onStart() 
//	{
//	        super.onStart();
//	        // Connect the client.
//	        mLocationClient.connect();
//	}
	@Override
	protected void onStart()
	{
		Log.d("asd","on start");
		super.onStart();
		mGoogleApiClient.connect();
	}
	@Override
	protected void onStop(){
		Log.d("asd","on stop");
		super.onStop();
		if(mGoogleApiClient.isConnected())
		{
			mGoogleApiClient.disconnect();
		}
	}

    @Override
    public void onConnectionFailed(ConnectionResult result) 
    {
    	Log.d("asd","onconnection failes");
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }
     
        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;
     
            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
		toolbar.setTitle("Posting Wall");
		setSupportActionBar(toolbar);
		
		
		ConnectivityManager conMgr  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = conMgr.getActiveNetworkInfo(); 
		if(info != null && info.isConnected()) 
		{}
		else
		{
			Toast.makeText(this,"No Internet connection",Toast.LENGTH_SHORT).show();
		}
		/*--checking for internet connection
		ConnectivityManager conMgr  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = conMgr.getActiveNetworkInfo(); 
		if(info != null && info.isConnected()) 
		{
		   
		}
		else
		{
			Toast.makeText(this,"No Internet connection",1).show();
		}
		 */
		//mLocationClient.connect();
//		mLocationClient = new LocationClient(this, this, this);
//		mCurrentLocation = mLocationClient.getLastLocation();
//		double lat;
//		double lng;
//		if(mCurrentLocation==null)
//		{
//			lat=0;
//			lng=0;
//		}
//		else
//		{
//			lat=mCurrentLocation.getLatitude();
//			lng=mCurrentLocation.getLongitude();
//			Log.d("Long",Double.toString(lat));
//			Log.d("Lat",Double.toString(lat));
//		}
		btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
	    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this).addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_LOGIN).build();
	    btnSignIn.setOnClickListener(this);
	    fb=(Button)findViewById(R.id.authButton);
		 if (savedInstanceState == null)
		    {
		        // Add the fragment on initial activity setup
		        loginFragment = new LoginFragment();
		        loginFragment.FBOnce=false;
		        getSupportFragmentManager()
		        .beginTransaction()
		        .add(loginFragment, "fbb")//.add(android.R.id.content, loginFragment)
		        .commit();
		    } 
		 	else 
		 	{
		        // Or set the fragment from restored state info
		        loginFragment = (LoginFragment) getSupportFragmentManager()
		        .findFragmentByTag("fbb");//.findFragmentById(android.R.id.content);
		    }
//	    Intent i=new Intent("android.action.LOGIN");
//		startActivity(i);
	}

    private void signInWithGplus() {
    	Log.d("asd","sign in with g plus");
        if (!mGoogleApiClient.isConnecting()) 
        {
        	Log.d("asd","sign in with g plus inside if");
        	mSignInClicked = true;
            resolveSignInError();
            Log.d("asd","sign in with g plus inside if complete");
        }
    }
	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.d("asd","onconnected");
		mSignInClicked = false;
		 getProfileInformation();
		
	}
	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		Log.d("asd","onconnectedsuspended");
		mGoogleApiClient.connect();
		
	}
    private void resolveSignInError() 
    {
    	Log.d("asd","resolve");
        if (mConnectionResult.hasResolution()) 
        {
        	
            try {
            	Log.d("asd","resolve inside try");
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(Login.this, RC_SIGN_IN);
                Log.d("asd","resolve inside try complete");
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
	 private void getProfileInformation() 
	 {
		 Log.d("asd","profile");
	        try 
	        {
	            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) 
	            {
	                Person currentPerson = Plus.PeopleApi
	                        .getCurrentPerson(mGoogleApiClient);
	                String personName = currentPerson.getDisplayName();
	                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
	     
//	                Log.e(TAG, "Name: " + personName + ", plusProfile: "
//	                        + personGooglePlusProfile + ", email: " + email);

	                Intent i=new Intent(Constants.homepage);
	    			startActivity(i);
	    			finish();
	                Toast.makeText(getApplicationContext(),personName+email,Toast.LENGTH_LONG).show();
	     
	            } else {
	                Toast.makeText(getApplicationContext(),
	                        "Person information is null", Toast.LENGTH_LONG).show();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    @Override
	    protected void onActivityResult(int requestCode, int responseCode,Intent intent)
	    {
	    	Log.d("asd","resolve");
	    	super.onActivityResult(requestCode,responseCode, intent);
	  	  	Session.getActiveSession().onActivityResult(this, requestCode,requestCode,intent);
	        if (requestCode == RC_SIGN_IN) 
	        {
	            if (responseCode != RESULT_OK) {
	                mSignInClicked = false;
	            }
	     
	            mIntentInProgress = false;
	     
	            if (!mGoogleApiClient.isConnecting()) 
	            {
	                mGoogleApiClient.connect();
	            }
	        }
	        	
	    }
	    private void revokeGplusAccess() 
	    {
	    	Log.d("asd","yet to enter revoke");
	        if (mGoogleApiClient.isConnected()) 
	        {
	        	Log.d("asd","revoke");
	            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
	                    .setResultCallback(new ResultCallback<Status>() 
	                    {
	                        @Override
	                        public void onResult(Status arg0) 
	                        {
	                            mGoogleApiClient.connect();
	                        }
	     
	                    });
	        }
	    }
	    private void signOutFromGplus() {
	        if (mGoogleApiClient.isConnected()) {
	            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
	            mGoogleApiClient.disconnect();
	            mGoogleApiClient.connect();
	        }
	    }
	    
	@Override
	public void onClick(View v)
	{
		   switch (v.getId()) 
		 {
	        case R.id.btn_sign_in:
	        {
	            // Signin button clicked
	        	Log.d("asd","on click");
	            signInWithGplus();
	            break;
	        }
//			case R.id.authButton:
//			{
//				 if (savedInstanceState == null)
//				    {
//				        // Add the fragment on initial activity setup
//				        loginFragment = new LoginFragment();
//				        loginFragment.FBOnce=false;
//				        getSupportFragmentManager()
//				        .beginTransaction()
//				        .add(android.R.id.content, loginFragment)
//				        .commit();
//				        
//				        
//				    } else 
//					{
//				        // Or set the fragment from restored state info
//				        loginFragment = (LoginFragment) getSupportFragmentManager()
//				        .findFragmentById(android.R.id.content);
//				    }
//					finish();
//				break;
//			}
		
		}
	}
	public void initPD() 
	 {
			pd=new ProgressDialog(Login.this);
			pd.setTitle("Logging in...");
			pd.setMessage("");
			pd.setIndeterminate(true);
			pd.setCancelable(false);
	 }
}