package me.cookly.ginrai;

/**
 * Created by Gulufuu on 11/26/2016 AD.
 */

public class FeedItem {
    public String dish;
    public String imageRef;
    public double lat;
    public double lon;
    public double timeSince;
    public String placeName;

    public FeedItem() {

    }

    public FeedItem(String dish, String ref, double lat, double lon, double timeSince, String placeName){
        this.dish = dish;
        this.imageRef = ref;
        this.lat = lat;
        this.lon = lon;
        this.timeSince = timeSince;
        this.placeName = placeName;
    }
}
