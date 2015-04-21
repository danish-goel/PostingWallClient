package com.example.postingwall.userpost;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.postingwall.R;
import com.example.postingwall.classes.Constants;
import com.example.postingwall.classes.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danish Goel on 01-Apr-15.
 */
public class MyPosts extends ActionBarActivity
{
    ProgressDialog pd;
    Context context=MyPosts.this;
    List<Post> userpost=new ArrayList<Post>();
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

        new BgTask().execute();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new CustomPostAdapter(userpost,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class BgTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            initPD();
            pd.show();
            userpost.clear();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Log.d("userpost_email",Constants.user_email);
            userpost.addAll(Constants.postService.getPostsForUser(Constants.user.getEmail()));
//            userpost=Constants.postService.getPostsForUser(Constants.user_email);
//            Log.d("userpost",userpost.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            try{pd.setCancelable(true);}catch(Exception e){}
            try{pd.dismiss();}catch(Exception e){}
            mAdapter.notifyDataSetChanged();
            if(userpost==null)
            {
                Toast.makeText(context,"null",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }

    }


    public void initPD()
    {
        pd=new ProgressDialog(context);
        pd.setTitle("Please Wait...");
        pd.setMessage("Fetching Your Posts");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
    }
}


