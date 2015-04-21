package com.example.postingwall;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.postingwall.classes.Constants;
import com.example.postingwall.classes.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danish Goel on 21-Apr-15.
 */
public class SinglePost extends Activity
{
    Post p= Constants.singlepost;
    List<String> comments=new ArrayList<String>();
    LinearLayout ll;
    ProgressBar pb;
    EditText comments_input;
    Button like;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_post);

        TextView title,content,poster;
        title=(TextView)findViewById(R.id.tv_title);
        content=(TextView)findViewById(R.id.tv_content);

        poster=(TextView)findViewById(R.id.tv_poster);
        pb=(ProgressBar)findViewById(R.id.progress_bar);
        ll=(LinearLayout)findViewById(R.id.ll);
        ImageButton submit=(ImageButton)findViewById(R.id.Submit);
        comments_input=(EditText)findViewById(R.id.comments_input);
//        like=(Button) findViewById(R.id.like);

        title.setText(p.getTitle());
        content.setText(p.getContent());
        poster.setText(p.getPoster());

//        like.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                String type=like.getText().toString();
//                if(type.equals("Like")) {
//                    new LikeTask(p.getId(), true).execute();
//                }
//                else
//                {
//                    new LikeTask(p.getId(),false).execute();
//                }
//            }
//        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String input = comments_input.getText().toString();
                comments_input.setText("");
                if (input.isEmpty()) {
                    Dialog d = new Dialog(SinglePost.this);
                    d.setTitle("Missing Comment");
                    TextView tv = new TextView(SinglePost.this);
                    tv.setText("\tEmpty comment is not allowed\n\n");
                    d.setContentView(tv);
                    d.show();
                } else {
                    new AddCommentTask(p.getId(),input).execute();
                }
            }

        });
            new  BgTask().execute();
    }

        public class BgTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
                comments=Constants.postService.getComments(p.getId());
               return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            pb.setVisibility(View.GONE);
            if(comments.isEmpty())
            {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.comment_list, null, false);
                TextView title = (TextView)v.findViewById(R.id.comment_content);
                title.setText("No Comments");
                ll.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            }
            else
            {
                for(String current:comments)
                {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = inflater.inflate(R.layout.comment_list, null, false);
                    TextView content = (TextView)v.findViewById(R.id.comment_content);
                    content.setText(current);
                    ll.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                }

            }
            super.onPostExecute(result);
        }

    }

    public class AddCommentTask extends AsyncTask<Void, Void, Void>
    {
        long id;
        String comment;

        AddCommentTask(long id,String comment)
        {
            this.id=id;
            this.comment=comment;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Constants.postService.addComment(id,comment);
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.comment_list, null, false);
            TextView content = (TextView)v.findViewById(R.id.comment_content);
            content.setText(comment);
            ll.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            super.onPostExecute(result);
        }

    }


    public class LikeTask extends AsyncTask<Void, Void, Void>
    {
        long id;
        boolean type;

        LikeTask(long id,boolean type)
        {
            this.id=id;
            this.type=type;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            if(type)
            {
                Constants.postService.addLike(id);
            }
            else
            {
                Constants.postService.addDisLike(id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
        }

    }
}
