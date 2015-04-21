package com.example.postingwall.classes;

public class Location
{
	private long id;
	private Double latitude,longitude;
	private String name;
    private User user;
    private String address;

	public Location()
	{
		
	}
	
	public Location(String name,Double latitude,Double longitude)
	{
		this.name=name;
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
