package com.example.postingwall.userlocation;

import android.content.Context;
import android.graphics.Color;
import android.location.Geocoder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Address;

import com.example.postingwall.R;
import com.example.postingwall.classes.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CustomLocationAdapter extends RecyclerView.Adapter<CustomLocationAdapter.ViewHolder>
{
    private List<Location> locations;
    Context context;
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
    public CustomLocationAdapter(List<Location> locations, Context context)
    {
        this.locations = locations;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        // create a new view

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_card_view, parent, false);

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
        ((TextView) holder.cardView.findViewById(R.id.location_name)).setText(locations.get(position).getName());
        ((TextView) holder.cardView.findViewById(R.id.location_content)).setText(getAddress(locations.get(position).getLatitude(),locations.get(position).getLongitude()));
        ((LinearLayout) holder.cardView.findViewById(R.id.header)).setBackgroundColor(Color.parseColor(getCardColor(position)));
        holder.cardView.setOnClickListener(new MyOnClickListener(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return locations.size();
    }

    class MyOnClickListener implements View.OnClickListener
    {

        int index;

        public MyOnClickListener(int position) {
            this.index = position;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, locations.get(this.index)+"", Toast.LENGTH_SHORT).show();
        }
    }

    public String getCardColor(int position)
    {
        int i=0;
        i=position%colors.size();
        Log.d("color",i+" "+colors.get(i));
        return colors.get(i);
    }

    public String getAddress(Double latitude,Double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses=new ArrayList<Address>();
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String city = addresses.get(0).getAddressLine(1);
            return city;
        }
        catch(Exception e)
        {
            return "";
        }

    }
}