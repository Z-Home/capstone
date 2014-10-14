package com.example.bryan.zHomeApplication;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Bryan on 10/13/2014.
 */
public class Thermostat implements Device {

    public Thermostat(HashMap<String, String> map, String devNum) {
    }

    @Override
    public void setValues(HashMap<String, String> map) {

    }

    @Override
    public HashMap<String, String> getValues() {
        return null;
    }

    @Override
    public void updateValues(String cc, String value) {

    }

    @Override
    public JSONObject command(String cc, String value) {
        return null;
    }
}
