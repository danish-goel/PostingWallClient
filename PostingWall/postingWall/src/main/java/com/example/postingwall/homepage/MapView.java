package com.example.postingwall.homepage;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.example.postingwall.R;
import com.example.postingwall.classes.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Danish Goel on 03-Apr-15.
 */
public class MapView extends ActionBarActivity
{
    GoogleMap googleMap;
    LatLng MyPoint = new LatLng(Constants.latitude,Constants.longitude);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /*--------------------------------Start Of Oncreate----------------------------------------------------------*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);

        /*--------------------------------Tooolbar----------------------------------------------------------*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Map View");
        setSupportActionBar(toolbar);
        try
        {
            if (googleMap == null)
            {
                googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(MyPoint).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));
            Marker othermarker;

            int post_iterator=0,places_iterator=0;
            while(!HomepageActivity.posts.isEmpty()&& post_iterator<HomepageActivity.posts.size())
            {
                String story= HomepageActivity.posts.get(post_iterator).getTitle();
                MyPoint = new LatLng(HomepageActivity.posts.get(post_iterator).getLatitude(),HomepageActivity.posts.get(post_iterator).getLongitude());
                othermarker = googleMap.addMarker(new MarkerOptions().position(MyPoint).title(story));
                post_iterator++;
            }

            while(!HomepageActivity.places.isEmpty()&& places_iterator<HomepageActivity.places.size())
            {
                String story= HomepageActivity.places.get(places_iterator).getName();
                MyPoint = new LatLng(HomepageActivity.places.get(places_iterator).getLatitude(),HomepageActivity.places.get(places_iterator).getLongitude());
                othermarker = googleMap.addMarker(new MarkerOptions().position(MyPoint).title(story));
                places_iterator++;
            }
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
