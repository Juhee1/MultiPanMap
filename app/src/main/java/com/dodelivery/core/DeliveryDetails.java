package com.dodelivery.core;

/**
 * Created by juhee on 27/5/17.
 */

public class DeliveryDetails {

    private String imageUrl;
    private String description;
    private Location location = new Location();

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
