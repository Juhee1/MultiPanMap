package com.dodelivery.core;

import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

/**
 * Created by juhee on 27/5/17.
 */

public class DeliveryDetailsParser implements Parser<Deliveries> {

    private JsonFactory jsonFactory = new JsonFactory();

    public static final String DESCRIPTION = "description";
    public static final String IMAGE_URL = "imageUrl";
    public static final String LOCATION = "location";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String ADDRESS = "address";

    private void throwParseException(Exception e) throws ParserException {
        Log.e("", e.toString());
        throw new ParserException("Could not parse DeliveyDetails: " + e.toString());
    }

    @Override
    public Deliveries parse(String raw) throws ParserException {
        if (raw == null || raw.isEmpty()) return null;
        try {
            Deliveries dataObject = new Deliveries();
            JsonParser parser = jsonFactory.createParser(raw);
            parser.nextToken();
            if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
                throwParseException(new Exception("JSON Start_Object not found"));
            } else if (parser.getCurrentToken() == JsonToken.START_ARRAY) {
                parseObject(parser, dataObject);

            }
            dataObject.setRawData(raw);
            return dataObject;
        } catch (Exception e) {
            Log.e("", e.toString());
        }
        return null;
    }

    private void parseObject(JsonParser parser, Deliveries dataObject) {
        try {
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
                    DeliveryDetails details = new DeliveryDetails();
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        if (DESCRIPTION.equals(parser.getCurrentName())) {
                            details.setDescription(parser.getText());
                        } else if (IMAGE_URL.equals(parser.getCurrentName())) {
                            details.setImageUrl(parser.getText());
                        } else if (LOCATION.equals(parser.getCurrentName())) {
                            if (parser.nextToken() == JsonToken.START_OBJECT) {
                                details.setLocation(parseLocation(parser));
                            }
                        }
                    }
                    dataObject.getDeliveryDetailsList().add(details);
                }
            }
        } catch (JsonParseException e) {
            throwParseException(e);
        } catch (IOException e) {
            throwParseException(e);
        }
    }

    private Location parseLocation(JsonParser parser) {
        Location loc = new Location();
        try {
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                if (LAT.equals(parser.getCurrentName())) {
                    loc.setLatitude(parser.getValueAsDouble());
                } else if (LNG.equals(parser.getCurrentName())) {
                    loc.setLongitude(parser.getValueAsDouble());
                } else if (ADDRESS.equals(parser.getCurrentName())){
                    loc.setAddress(parser.getText());
                }
            }
        } catch (JsonParseException e) {
            throwParseException(e);
        } catch (IOException e) {
            throwParseException(e);
        }
        return loc;
    }

}
