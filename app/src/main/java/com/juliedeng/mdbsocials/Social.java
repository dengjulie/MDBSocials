package com.juliedeng.mdbsocials;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Model class for each social/event. Details for each social all contained in the class variables.
 */

public class Social implements Serializable, Comparable<Social> {
    String name, description, email, imageURL, date, firebaseKey;
    int numInterested;
    long timestamp;
    ArrayList<String> interestedEmails;

    public Social() {
        this.name = null;
        this.description = null;
        this.email = null;
        this.imageURL = null;
        this.timestamp = 0;
        this.date = null;
        this.numInterested = 0;
        interestedEmails = null;
        this.firebaseKey = null;
    }

    public Social(String name, String description, String email, String imageURL, long timestamp, String date, String firebaseKey) {
        this.name = name;
        this.description = description;
        this.email = email;
        this.imageURL = imageURL;
        this.timestamp = timestamp;
        this.date = date;
        this.numInterested = 1;
        this.interestedEmails = new ArrayList<>();
        this.interestedEmails.add(email);
        this.firebaseKey = firebaseKey;
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

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public ArrayList<String> getInterestedEmails() {
        return interestedEmails;
    }
}
