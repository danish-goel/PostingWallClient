package com.example.postingwall.classes;

import java.util.ArrayList;

public class Places 
{
	String name;
	String vicinity;
	ArrayList<String>types;
	String rating;
    Double latitude;
    Double longitude;

	public Places()
	{
		name="";
		vicinity="";
		rating="";
		types=null;
	}
	
	@Override
	public String toString() 
	{
		// TODO Auto-generated method stub
		StringBuilder description=new StringBuilder();
		description.append("Name:"+this.getName()+"\n");
		description.append("Vicinity:"+this.getVicinity()+"\n");
		description.append("Types:"+this.getTypes().toString()+"\n");
		return description.toString();
	}

	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getRating() 
	{
		return rating;
	}
	
	public void setRating(String rating) 
	{
		this.rating = rating;
	}
	
	public String getVicinity() 
	{
		return vicinity;
	}
	
	public void setVicinity(String vicinity) 
	{
		this.vicinity = vicinity;
	}

	public ArrayList<String> getTypes() 
	{
		return types;
	}
	
	public void setTypes(ArrayList<String> types) 
	{
		this.types = types;
	}
	
	public String getTypeString()
	{
		String resultType="";
		for(String s:types)
		{
			resultType+=Constants.capitaliseFirstLetter(s)+",";
		}
		return resultType;
	}

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
