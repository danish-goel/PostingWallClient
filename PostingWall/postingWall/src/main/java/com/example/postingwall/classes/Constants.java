package com.example.postingwall.classes;

import android.content.Intent;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

public class Constants
{
    public static float radius=2;
    public static Double latitude,longitude;//
    public static Double current_latitude,current_longitude;
    public static String user_name;
    public static String user_email;
    public static User user;
    public static String gcm_regid;

    public static final String getlocation="com.example.postingwall.getLocation";
    public static final String addpost="com.example.postingwall.addpost.NewPost";
    public static final String homepage="com.example.postingwall.homepage.Homepage";
    public static final String TEST_URL = "http://192.168.8.100:8080";
//    public static final String TEST_URL = "http://192.168.43.61:8080";
//    public static final String TEST_URL = "http://192.168.49.6:8080";
    public static final String login="com.example.postingwall.login.Login";
    public static final String myposts="com.example.postingwall.userpost.MyPosts";
    public static final String mylocations="com.example.postingwall.userlocation.MyLocations";
    public static final String mapview="com.example.postingwall.homepage.MapView";
    public static final String locationmap="com.example.postingwall.selectonmap.MapLocation";

    public static Intent getIntent(String s)
    {
        Intent i=new Intent(s);
        return i;
    }

    public static PostSvcApi postService = new RestAdapter.Builder()
            .setEndpoint(TEST_URL)
            .setLogLevel(LogLevel.FULL)
            .build()
            .create(PostSvcApi.class);

    public static String capitaliseFirstLetter(String string)
    {
        StringBuilder sb = new StringBuilder(string);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
    public static long calculateDistance(double userLat, double userLng,double venueLat, double venueLng)
    {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (long) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));
    }

    public static String[] getDrawerItems()
    {
        String mTitles[];
        mTitles = new String[]{"Home","My posts","My Locations"};
        return mTitles;
    }

    public static Intent selectItem(int position)
    {

        //Toast.makeText(getApplicationContext(), mTitles[position], 0).show();

        // Highlight the selected item, update the title, and close the drawer
        //mDrawerList.setItemChecked(position, true);
        if(position ==0)
        {
            Intent askquestion=new Intent(homepage);
            return askquestion;
        }
        if(position==1)
        {
            Intent askquestion=new Intent(myposts);
            return askquestion;
        }
        if(position==2)
        {
            Intent askquestion=new Intent(mylocations);
            return askquestion;
        }
        return null;

    }

}
