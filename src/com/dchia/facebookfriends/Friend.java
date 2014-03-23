package com.dchia.facebookfriends;

/* Facebook Friend object */
public class Friend {
	private String id;
    private String name;
    
    final static String URL_PREFIX = "http://graph.facebook.com/";
    final static String URL_SUFFIX = "/picture";
 
    // constructor
    public Friend(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // get URL of profile picture
    public String getPicUrl() {
        return URL_PREFIX + id + URL_SUFFIX;
    }
    
    // get Facebook ID
    public String getId() {
        return id;
    }
    
    // set Facebook ID - currently unused
    public void setId(String id) {
        this.id = id;
    }
    
    // get full name
    public String getName() {
        return name;
    }
    
    // set name - currently unused
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
