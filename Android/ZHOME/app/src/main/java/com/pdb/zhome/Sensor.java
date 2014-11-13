package com.pdb.zhome;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Bryan on 10/13/2014.
 */
public class Sensor implements Device {

    private String type;
    private String devNum;

    public Sensor(HashMap<String, String> map, String devNum) {
        this.devNum = devNum;
        this.type = "sensor";
    }

    @Override
    public String getType(){return this.type;}

    @Override
    public String getDevName() {
        return "Sensor";
    }

    @Override
    public String getDevNum() {return this.devNum;}

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

    @Override
    public String[] formatUIinfo(String cc, String value) {
        return null;
    }
}
