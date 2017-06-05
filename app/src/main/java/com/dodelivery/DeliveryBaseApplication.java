package com.dodelivery;

import android.app.Application;

import com.dodelivery.core.Deliveries;
import com.dodelivery.core.DeliveryDetailsParser;

/**
 * Created by juhee on 27/5/17.
 */

public class DeliveryBaseApplication extends Application {

    private static DeliveryBaseApplication instance = null;

    public static DeliveryBaseApplication instance(){
        return instance;
    }

    String delivery ="[{\n" +
            "\t\"description\": \"Deliver documents to Chicago\",\n" +
            "\t\"imageUrl\": \"image_1\",\n" +
            "\t\"location\": {\n" +
            "\t\t\"lat\":  41.85,\n" +
            "\t\t\"lng\": -87.65,\n" +
            "\t\t\"address\": \"Chicago\"\n" +
            "\t}\n" +
            "}, {\n" +
            "\t\"description\": \"Deliver parcel to Jaipur (India)\",\n" +
            "\t\"imageUrl\": \"image_2\",\n" +
            "\t\"location\": {\n" +
            "\t\t\"lat\": 26.922070,\n" +
            "\t\t\"lng\": 75.778885,\n" +
            "\t\t\"address\": \"Jaipur\"\n" +
            "\t}\n" +
            "}]";

    public Deliveries deliveries = new Deliveries();

    public void onCreate(){
        super.onCreate();
        DeliveryBaseApplication.instance = this;
        DeliveryDetailsParser parser = new DeliveryDetailsParser();
        deliveries = parser.parse(delivery);
    }

    public Deliveries getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(Deliveries deliveries) {
        this.deliveries = deliveries;
    }
}
