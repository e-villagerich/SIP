package UI;

import android.net.Uri;

/**
 * Created by etv on 11/29/2017.
 */

public class ActivityFeeds {

    private String name;
    private String photoURL;
    private String activity;
    private String details;
    private String adate;

    public ActivityFeeds(){

    }
    public ActivityFeeds(String name, String photoURL, String activity, String details, String adate){
        this.name = name;
        this.photoURL = photoURL;
        this.activity = activity;
        this.details = details;
        this.adate = adate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAdate() {
        return adate;
    }

    public void setAdate(String adate) {
        this.adate = adate;
    }

}
