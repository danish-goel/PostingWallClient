package com.example.postingwall.homepage;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.postingwall.R;
import com.example.postingwall.classes.Constants;
import com.example.postingwall.classes.Places;
import com.example.postingwall.classes.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomPostAdapter extends RecyclerView.Adapter<CustomPostAdapter.ViewHolder>
{
    private List<Post> posts;
    private List<Places> places;
    Context context;
//    List<Integer> colors=new ArrayList<Integer>(
//            Arrays.asList(R.color.greenishblue,R.color.purple,R.color.red,R.color.blue,R.color.green,R.color.limegreen,R.color.orange));

    List<String> colors=new ArrayList<String>(
            Arrays.asList("#009688","#9C27B0","#F44336","#8BC34A","#CDDC39","#FF9800","#9E9E9E"));

    private final int VIEW_PLACE = 0;
    private final int VIEW_POST= 1;

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
    public CustomPostAdapter(List<Post> posts,List<Places> places, Context context)
    {
        this.posts = posts;
        this.context = context;
        this.places=places;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        // create a new view
        if(viewType==VIEW_POST) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_card_view, parent, false);

            // set the view's size, margins, paddings and layout parameters
            //...
            ViewHolder vh = new ViewHolder((CardView) v);
            return vh;
        }
        else
        {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.place_card_view, parent, false);

            // set the view's size, margins, paddings and layout parameters
            //...
            ViewHolder vh = new ViewHolder((CardView) v);
            return vh;
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);

        if(getItemViewType(position)==VIEW_POST)
        {
            Log.d("postition", "post" + position);
            ((TextView) holder.cardView.findViewById(R.id.tv_title)).setText(posts.get(position).getTitle());
            ((TextView) holder.cardView.findViewById(R.id.tv_content)).setText(posts.get(position).getContent());
            ((TextView) holder.cardView.findViewById(R.id.tv_poster)).setText("by:" + posts.get(position).getPoster());
            ((TextView) holder.cardView.findViewById(R.id.tv_distance)).setText(Constants.calculateDistance(Constants.latitude, Constants.longitude, posts.get(position).getLatitude(), posts.get(position).getLongitude()) + "km far");
            ((LinearLayout) holder.cardView.findViewById(R.id.header)).setBackgroundColor(Color.parseColor(getCardColor(position)));
            holder.cardView.setOnClickListener(new MyOnClickListener(position));
        }
        else
        {
            int pos=position-posts.size();
            Log.d("postition","place"+position+" pos:"+pos);
            ((TextView) holder.cardView.findViewById(R.id.place_type)).setText(places.get(pos).getTypeString());
            ((TextView) holder.cardView.findViewById(R.id.place_title)).setText(places.get(pos).getName());
            ((TextView) holder.cardView.findViewById(R.id.place_address)).setText(places.get(pos).getVicinity());
            ((LinearLayout) holder.cardView.findViewById(R.id.header)).setBackgroundColor(Color.parseColor(getCardColor(position)));
        }

    }

    @Override
    public int getItemViewType(int position)
    {

        if (position<posts.size())
        {
            return VIEW_POST;
        }
        else
        {
            return VIEW_PLACE;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        Log.d("count",posts.size()+places.size()+"");
        return posts.size()+places.size();
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

    public String getCardColor(int position)
    {
        int i=0;
        i=position%colors.size();
        Log.d("color",i+" "+colors.get(i));
        return colors.get(i);
    }

}
