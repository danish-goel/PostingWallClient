package com.example.postingwall.selectonmap;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.postingwall.R;
import com.example.postingwall.classes.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Danish Goel on 03-Apr-15.
 */
public class MapLocation extends ActionBarActivity
{
    Context context=MapLocation.this;

    GoogleMap googleMap;
    LatLng MyPoint = new LatLng(Constants.current_latitude,Constants.current_longitude);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Select Location");
        setSupportActionBar(toolbar);

        final TextView location_address=(TextView)findViewById(R.id.location_address);
        location_address.setVisibility(View.VISIBLE);

        try
        {
            if (googleMap == null)
            {
                googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(MyPoint).draggable(true).title("Current Location"));
            marker.setDraggable(true);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));
            googleMap.setOnMarkerDragListener(new OnMarkerDragListener()
            {

                @Override public void onMarkerDragStart(Marker arg0) {}

                @Override
                public void onMarkerDragEnd(Marker arg0)
                {
                    String address=getAddress(arg0.getPosition().latitude, arg0.getPosition().longitude, context);
                    Constants.latitude=arg0.getPosition().latitude;
                    Constants.longitude=arg0.getPosition().longitude;
                    location_address.setText(address);
                }

                @Override public void onMarkerDrag(Marker arg0) { }

            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
            return "";
        }

    }
}
