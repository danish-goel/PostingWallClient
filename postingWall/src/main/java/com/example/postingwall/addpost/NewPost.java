package com.example.postingwall.addpost;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.postingwall.R;
import com.example.postingwall.classes.Constants;
import com.example.postingwall.classes.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class NewPost extends ActionBarActivity implements OnClickListener
{
	EditText postContent,postTitle;
    TextView address;
	Button submit;
	ProgressDialog pd;
	Context context=NewPost.this;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_post);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
		toolbar.setTitle("New Post");
		setSupportActionBar(toolbar);
		
		postTitle=(EditText)findViewById(R.id.postTitle);
		postContent=(EditText)findViewById(R.id.postContent);
        address=(TextView)findViewById(R.id.address);
		submit=(Button)findViewById(R.id.submit);
		submit.setOnClickListener(this);

        new AddressTask().execute();
	}
	
	
	@Override
	public void onClick(View v) 
	{
		if(v.getId()==R.id.submit)
		{
			new BgTask().execute();
		}
		
	}	
	
	public class BgTask extends AsyncTask<Void, Void, Void>
	 {
		 
		 @Override
		protected void onPreExecute() 
		 {
			 initPD();
			 pd.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) 
		{
			String title =postTitle.getText().toString();
			String post =postContent.getText().toString();
			Post postObject = new Post(title,post,Constants.user_name,Constants.latitude,Constants.longitude);
            postObject.setUser(Constants.user);
			boolean ok = Constants.postService.addPost(postObject);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) 
		{
			try{pd.setCancelable(true);}catch(Exception e){}
			try{pd.dismiss();}catch(Exception e){}
            Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
		}
		 
	 }


    public class AddressTask extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... params)
        {
            String add=getAddress(Constants.latitude,Constants.longitude,context);
            Log.d("asd",add);
            return add;
        }

        protected void onPostExecute(String result)
        {
            address.setText(result);
        }

    }
		
	
	 public void initPD() 
	 {
			pd=new ProgressDialog(context);
			pd.setTitle("Please Wait...");
			pd.setMessage("Fetching Data");
			pd.setIndeterminate(true);
			pd.setCancelable(false);
	 }

    public static String getAddress(Double latitude,Double longitude,Context context)
    {
        Geocoder geocoder;
        List<Address> addresses=new ArrayList<Address>();
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getAddressLine(1);
            return address+","+city;
        }
        catch(Exception e)
        {
            return "Not Found";
        }

    }

}
