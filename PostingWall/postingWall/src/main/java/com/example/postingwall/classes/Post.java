package com.example.postingwall.classes;
public class Post 
{
	private long id;
	private String title;
	private String content;
	private String poster;
	private Double latitude,longitude;
    private User user;

    public Post()
	{
		
	}

	public Post(String title, String content,String poster,Double latitude,Double longitude) 
	{
		super();
		this.title=title;
		this.content=content;
		this.poster=poster;
		this.latitude=latitude;
		this.longitude=longitude;
	}

	public long getId() 
	{
		return id;
	}

	public void setId(long id) 
	{
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
