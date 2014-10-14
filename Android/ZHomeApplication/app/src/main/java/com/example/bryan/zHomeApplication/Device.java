package com.example.bryan.zHomeApplication;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Bryan on 10/13/2014.
 */
public interface Device {
    void setValues(HashMap<String,String> map);
    HashMap<String,String> getValues();
    void updateValues(String cc, String value);
    JSONObject command(String cc, String value);
}
