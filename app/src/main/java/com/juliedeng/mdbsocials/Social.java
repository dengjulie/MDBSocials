package com.juliedeng.mdbsocials;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by juliedeng on 2/20/18.
 */

public class Social implements Serializable, Comparable<Social> {
    String name, description, email, imageURL, date;
    int numInterested;
    long timestamp;

    public Social() {
        this.name = null;
        this.description = null;
        this.email = null;
        this.imageURL = null;
        this.timestamp = 0;
        this.date = null;
        this.numInterested = 0;
    }

    public Social(String name, String description, String email, String imageURL, long timestamp, String date) {
        this.name = name;
        this.description = description;
        this.email = email;
        this.imageURL = imageURL;
        this.timestamp = timestamp;
        this.date = date;
        this.numInterested = 1;
    }
    public int compareTo(Social anotherInstance) {
        if (this.timestamp > anotherInstance.timestamp) {
            return 1;
        } else {
            return -1;
        }
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

    public String getDate() {
        return date;
    }

    public int getNumInterested() {
        return numInterested;
    }

}
