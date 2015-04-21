package com.example.postingwall.classes;

/**
 * Created by Danish Goel on 01-Apr-15.
 */

public class User
{
    private long id;
    private String email;
    private String name;
    String gcm_regid;

    // We add a OneToMany annotation indicating that each Category (e.g., "the one")
    // can refer to multiple videos (e.g., "the many"). The "mappedBy"
    // attribute tells JPA the name of the property on the Video object
    // that refers to a category (e.g., Video.category). In this case,
    // the relationship is bi-directional since both the Video and the
    // Category know about each other. It is also possible for only one
    // class to be aware of the other.
    //
    // You do not have to create the table for this class before using @OneToMany.
    // The needed tables will automatically be created for you.

    public User(String email,String name,String gcm_regid)
    {
        super();
        this.email=email;
        this.name=name;
        this.gcm_regid=gcm_regid;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGcm_regid() {
        return gcm_regid;
    }

    public void setGcm_regid(String gcm_regid) {
        this.gcm_regid = gcm_regid;
    }


}
