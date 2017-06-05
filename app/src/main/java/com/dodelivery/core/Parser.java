package com.dodelivery.core;

import android.net.ParseException;

/**
 * Created by juhee on 27/5/17.
 */

public interface Parser<T> {
    T parse(String raw) throws ParseException;
}