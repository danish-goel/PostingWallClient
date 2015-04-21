package com.example.postingwall.userpost;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.postingwall.R;
import com.example.postingwall.classes.Constants;
import com.example.postingwall.classes.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomPostAdapter extends RecyclerView.Adapter<CustomPostAdapter.ViewHolder>
{
    private List<Post> posts;
    Context context;
//    List<Integer> colors=new ArrayList<Integer>(
//            Arrays.asList(R.color.greenishblue,R.color.purple,R.color.red,R.color.blue,R.color.green,R.color.limegreen,R.color.orange,R.color.grey));
    List<String> colors=new ArrayList<String>(
            Arrays.asList("#009688","#9C27B0","#F44336","#8BC34A","#CDDC39","#FF9800","#9E9E9E"));

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        //public TextView mTextView;
        public CardView cardView;
        public ViewHolder(CardView v)
        {
            super(v);
            cardView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomPostAdapter(List<Post> posts, Context context)
    {
        this.posts = posts;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        // create a new view

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_card_view, parent, false);

            // set the view's size, margins, paddings and layout parameters
            //...
            ViewHolder vh = new ViewHolder((CardView) v);
            return vh;


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
            Log.d("postition", "post" + position);
            ((TextView) holder.cardView.findViewById(R.id.tv_title)).setText(posts.get(position).getTitle());
            ((TextView) holder.cardView.findViewById(R.id.tv_content)).setText(posts.get(position).getContent());
            ((TextView) holder.cardView.findViewById(R.id.tv_poster)).setText("by:" + posts.get(position).getPoster());
            ((TextView) holder.cardView.findViewById(R.id.tv_distance)).setText(Constants.calculateDistance(Constants.latitude, Constants.longitude, posts.get(position).getLatitude(), posts.get(position).getLongitude()) + "km far");
            ((LinearLayout) holder.cardView.findViewById(R.id.header)).setBackgroundColor(Color.parseColor(getCardColor(position)));
            ((Button)holder.cardView.findViewById(R.id.delete_button)).setVisibility(View.VISIBLE);
            holder.cardView.findViewById(R.id.delete_button).setOnClickListener(new DeleteListener(position));
            holder.cardView.setOnClickListener(new MyOnClickListener(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    class MyOnClickListener implements View.OnClickListener
    {

        int index;

        public MyOnClickListener(int position) {
            this.index = position;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, posts.get(this.index)+"", Toast.LENGTH_SHORT).show();
        }
    }

    class DeleteListener implements View.OnClickListener
    {

        int index;

        public DeleteListener(int position) {
            this.index = position;
        }

        @Override
        public void onClick(View v)
        {
            new BgTask().execute(posts.get(this.index));
        }
    }


    public String getCardColor(int position)
    {
        int i=0;
        i=position%colors.size();
        Log.d("color",i+" "+colors.get(i));
        return colors.get(i);
    }

    public class BgTask extends AsyncTask<Post,Void,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Post... params)
        {
            Post p=params[0];
            Constants.postService.deletePost(p.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }

    }

}
