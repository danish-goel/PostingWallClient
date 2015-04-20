package com.example.postingwall.homepage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.postingwall.R;
import com.example.postingwall.classes.Constants;
import com.example.postingwall.classes.Location;
import com.example.postingwall.classes.Places;
import com.example.postingwall.classes.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomepageActivity extends ActionBarActivity implements OnClickListener
{
    Button newstory;
    ProgressDialog pd;
    Context context=HomepageActivity.this;

    /*-----------Drawer items---------*/
    RecyclerView mRightNavigationDrawerRecyclerView;
    String[] mTitles;
    private DrawerLayout mDrawerLayout;
    static ActionBarDrawerToggle drawerToggle;
	/*---------------------------*/

    public static List<Post> posts=new ArrayList<Post>();
    public static List<Places> places=new ArrayList<Places>();
    List<Location> userlocation=new ArrayList<Location>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeLayout;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /*--------------------------------Start Of Oncreate----------------------------------------------------------*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        /*--------------------------------Tooolbar----------------------------------------------------------*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Posting Wall");
        setSupportActionBar(toolbar);

        newstory=(Button)findViewById(R.id.newstorybutton);
        newstory.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CustomPostAdapter(posts,places,this);
        mRecyclerView.setAdapter(mAdapter);

        new BgTask().execute();


    /*------------------------Swipe Refresh Layout------------------------------------------------------------------*/


        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new RefreshTask().execute();
            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    /*---------------------------Navigation Drawer-----------------------------------------------------------*/

        mTitles = Constants.getDrawerItems();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setLeftDrawer();
        drawerToggle= new ActionBarDrawerToggle(this, mDrawerLayout,toolbar, R.string.app_name, R.string.app_name)
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(drawerToggle);
        // Set the list's click listener
        drawerToggle.syncState();

    /*--------------------------------End Of Oncreate----------------------------------------------------------*/

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.MapView)
        {
            startActivity(Constants.getIntent(Constants.mapview));
            return true;
        }
        else if (id == R.id.change_radius)
        {
            changeRadius();
            return true;
        }
        else if (id == R.id.changeLocation)
        {
            new FetchLocationTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*---------------------------------------Bg Task---------------------------------------------------*/
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
            posts.addAll(Constants.postService.getNearbyPostList(Constants.latitude,Constants.longitude,Constants.radius));
            places.addAll(Constants.postService.getNearbyPlacesList(Constants.latitude,Constants.longitude,Constants.radius));
            Log.d("list",places.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            mAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
            try{pd.setCancelable(true);}catch(Exception e){}
            try{pd.dismiss();}catch(Exception e){}
            super.onPostExecute(result);
        }

    }
    /*----------------------------------Refresh Task--------------------------------------------------------*/

    public class RefreshTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            swipeLayout.setRefreshing(true);
            posts.clear();
            places.clear();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Collection<Post> postList;
            posts.addAll(Constants.postService.getNearbyPostList(Constants.latitude,Constants.longitude,Constants.radius));
            places.addAll(Constants.postService.getNearbyPlacesList(Constants.latitude,Constants.longitude,Constants.radius));
            Log.d("list",places.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            mAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
            super.onPostExecute(result);
        }

    }
    /*----------------------------------Initializing Progress Dialog--------------------------------------------------------*/
    public void initPD()
    {
        pd=new ProgressDialog(context);
        pd.setTitle("Please Wait...");
        pd.setMessage("Fetching Data");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
    }
    /*----------------------------------OnClick Listener--------------------------------------------------------*/
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.newstorybutton)
        {
            startActivity(Constants.getIntent(Constants.addpost));
        }

    }

    /*----------------------------------Left Drawer--------------------------------------------------------*/
    public void setLeftDrawer()
    {
        String TITLES[] = Constants.getDrawerItems();
        int ICONS[] = {R.drawable.ic_home,R.drawable.ic_friend,R.drawable.ic_friend};

        //Similarly we Create a String Resource for the name and email in the header view
        //And we also create a int resource for profile picture in the header view

        String NAME =Constants.user_name;
        String EMAIL =Constants.user_email;
        int PROFILE = R.drawable.user;
        mRightNavigationDrawerRecyclerView = (RecyclerView) findViewById(R.id.LeftNavigationDrawerRecyclerView); // Assigning the RecyclerView Object to the xml View
        mRightNavigationDrawerRecyclerView.setHasFixedSize(true);
        NavigationDrawerAdapter mRightNavigationDrawerAdapter = new NavigationDrawerAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);
        mRightNavigationDrawerRecyclerView.setAdapter(mRightNavigationDrawerAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(HomepageActivity.this, new GestureDetector.SimpleOnGestureListener()
        {

            @Override public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }

        });

        mRightNavigationDrawerRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent)
            {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());



                if(child!=null && mGestureDetector.onTouchEvent(motionEvent))
                {
                    mDrawerLayout.closeDrawer(mRightNavigationDrawerRecyclerView);
                    int position=recyclerView.getChildPosition(child);
                    Intent i=Constants.selectItem(position-1);
                    startActivity(i);
                    return true;

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub

            }
        });

//		        mRightNavigationDrawerRecyclerView.setOnClickListener(new RightDrawerItemClickListener());

        RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(this);                 // Creating a layout Manager

        mRightNavigationDrawerRecyclerView.setLayoutManager(mLayoutManager);
    }


    private void changeRadius()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Enter the name of the Location\n"); //Set Alert dialog title here

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(Constants.radius+"");
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                Float newRadius = Float.parseFloat(input.getEditableText().toString());
                Constants.radius=newRadius;
                new RefreshTask().execute();
                Log.d("newRadius",Constants.radius+"");
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

    public class FetchLocationTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            userlocation.clear();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            userlocation.addAll(Constants.postService.getLocationsForUser(Constants.user.getEmail()));
//            Log.d("userpost",userpost.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setTitle("Locations");
            dialog.show();
            rg=(RadioGroup)dialog.findViewById(R.id.radiogroup);

            RadioButton rb_current=new RadioButton(context);
            rb_current.setText("Current Location");
            rg.addView(rb_current);
            RadioButton rb_map=new RadioButton(context);
            rb_map.setText("Select on Map");
            rg.addView(rb_map);

            for(Location l:userlocation)
            {
                RadioButton rb=new RadioButton(context);
                rb.setText(l.getName());
                rg.addView(rb);
            }
            Button rbSubmit=(Button)dialog.findViewById(R.id.rbSubmit);
            rbSubmit.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                        getLocationForRadioButton(rg,dialog);
                }
            });
            super.onPostExecute(result);
        }

    }

    public void getLocationForRadioButton(RadioGroup rg,Dialog dg)
    {
        if(rg.getCheckedRadioButtonId()!=-1)
        {
            int id= rg.getCheckedRadioButtonId();
            View radioButton = rg.findViewById(id);
            int radioId = rg.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rg.getChildAt(radioId);
            String selection = (String) btn.getText();
            if(selection.equals("Current Location"))
            {
                Constants.latitude=Constants.current_latitude;
                Constants.longitude=Constants.current_longitude;
                new RefreshTask().execute();
            }
            if(selection.equals("Select on Map"))
            {
                startActivity(Constants.getIntent(Constants.locationmap));
                new RefreshTask().execute();
            }
            for(Location l:userlocation)
            {
                if(l.getName().equals(selection))
                {
                    Constants.latitude=l.getLatitude();
                    Constants.longitude=l.getLongitude();
                    new RefreshTask().execute();
                }
            }
            dg.dismiss();
        }
    }


    /*----------------------------------**END***--------------------------------------------------------*/

}