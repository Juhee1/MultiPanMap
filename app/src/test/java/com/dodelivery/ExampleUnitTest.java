package com.dodelivery;

import com.dodelivery.core.Deliveries;
import com.dodelivery.core.DeliveryDetailsParser;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void parse_isCorrect() throws Exception {
        String delivery ="[{\n" +
                "\t\"description\": \"Deliver documents to Andrio\",\n" +
                "\t\"imageUrl\": \"http://placekitten.com.s3.amazonaws.com/homepage-samples/200/287.jpg\",\n" +
                "\t\"location\": {\n" +
                "\t\t\"lat\": 22.336093,\n" +
                "\t\t\"lng\": 114.155288,\n" +
                "\t\t\"address\": \"Cheung Sha Wan\"\n" +
                "\t}\n" +
                "}, {\n" +
                "\t\"description\": \"Deliver parcel to Leviero\",\n" +
                "\t\"imageUrl\": \"http://placekitten.com.s3.amazonaws.com/homepage-samples/200/286.jpg\",\n" +
                "\t\"location\": {\n" +
                "\t\t\"lat\": 22.319181,\n" +
                "\t\t\"lng\": 114.170008,\n" +
                "\t\t\"address\": \"Mong Kok\"\n" +
                "\t}\n" +
                "}]";

        DeliveryDetailsParser parser = new DeliveryDetailsParser();
        Deliveries x = parser.parse(delivery);
        Assert.assertNotNull(x);
    }
}