package com.example.postingwall.userlocation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.postingwall.R;
import com.example.postingwall.classes.Constants;
import com.example.postingwall.classes.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danish Goel on 02-Apr-15.
 */
public class MyLocations extends ActionBarActivity
{
    ProgressDialog pd;
    Context context=MyLocations.this;
    List<Location> userlocation=new ArrayList<Location>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_posts);

        /*--------------------------------Tooolbar----------------------------------------------------------*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("My posts");
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new CustomLocationAdapter(userlocation,this);
        mRecyclerView.setAdapter(mAdapter);

        new BgTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.save_location)
        {
            saveLocation();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            Log.d("userpost_email", Constants.user_email);
            userlocation.clear();
            userlocation.addAll(Constants.postService.getLocationsForUser(Constants.user.getEmail()));
//            Log.d("userpost",userpost.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            try{pd.setCancelable(true);}catch(Exception e){}
            try{pd.dismiss();}catch(Exception e){}
            mAdapter.notifyDataSetChanged();
            if(userlocation==null)
            {
                Toast.makeText(context, "null", Toast.LENGTH_LONG).show();
            }
            Log.d("location",userlocation.toString());
            super.onPostExecute(result);
        }

    }


    public void initPD()
    {
        pd=new ProgressDialog(context);
        pd.setTitle("Please Wait...");
        pd.setMessage("Fetching Your Locations");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
    }

    private void saveLocation()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Enter the name of the Location\n"); //Set Alert dialog title here

        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String srt = input.getEditableText().toString();
                Location newlocation=new Location(srt,Constants.latitude,Constants.longitude);
                newlocation.setUser(Constants.user);
                new SaveLocationTask().execute(newlocation);
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }


    public class SaveLocationTask extends AsyncTask<Location, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Location... params)
        {
            Location l= params[0];
            Constants.postService.addLocation(l);
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
        }

    }
}
