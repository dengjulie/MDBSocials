package com.juliedeng.mdbsocials;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by juliedeng on 2/20/18.
 */

public class Social implements Serializable {
    String name, description, email, imageURL;
    Object timestamp;
    int numInterested;
    ArrayList<String> peopleInterested;

    public Social() {
        this.name = null;
        this.description = null;
        this.email = null;
        this.imageURL = null;
        this.timestamp = null;
        this.numInterested = 0;
        this.peopleInterested = null;
    }

    public Social(String name, String description, String email, String imageURL, Object timestamp, ArrayList<String> peopleInterested) {
        this.name = name;
        this.description = description;
        this.email = email;
        this.imageURL = imageURL;
        this.timestamp = timestamp;
        this.numInterested = 1;
        this.peopleInterested = peopleInterested;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public int getNumInterested() {
        return numInterested;
    }

    public ArrayList<String> getPeopleInterested() {
        return peopleInterested;
    }

}