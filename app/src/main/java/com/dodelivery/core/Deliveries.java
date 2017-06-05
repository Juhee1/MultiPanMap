package com.dodelivery.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juhee on 27/5/17.
 */

public class Deliveries {

    private List<DeliveryDetails> deliveryDetailsList = new ArrayList<>();
    private String rawData;


    public List<DeliveryDetails> getDeliveryDetailsList() {
        return deliveryDetailsList;
    }

    public void setDeliveryDetailsList(List<DeliveryDetails> deliveryDetailsList) {
        this.deliveryDetailsList = deliveryDetailsList;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}
