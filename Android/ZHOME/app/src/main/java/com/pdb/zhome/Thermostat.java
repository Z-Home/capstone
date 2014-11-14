package com.pdb.zhome;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Bryan on 10/13/2014.
 */
public class Thermostat implements Device {

    private String devNum;
    private String type;
    private String devName;

    public Thermostat(HashMap<String, String> map, String devNum, String devName) {
        this.type = "thermostat";
        setDevNum(devNum);
        setDevName(devName);
    }

    public void setDevNum(String devNum) {
        this.devNum = devNum;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    @Override
    public void setValues(HashMap<String, String> map) {

    }

    @Override
    public String getType(){return this.type;}

    @Override
    public String getDevName() {
        return this.devName;
    }

    @Override
    public String getDevNum() {return this.devNum;}

    @Override
    public HashMap<String, String> getValues() {
        return null;
    }

    @Override
    public void updateValues(String cc, String value) {

    }

    @Override
    public JSONObject command(String cc, String value) {
        JSONObject jsonToSend, json;
        jsonToSend = new JSONObject();
        json = new JSONObject();

        if(cc.equals("66")){
            cc = "64";
        }

        try {
            json.put("device", this.devNum);
            json.put("commandClass", cc);
            json.put("command", value);
            jsonToSend.put("Type", "Command");
            jsonToSend.put("Json", json.toString());
            System.out.println("Sending JSON: " + jsonToSend.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonToSend;
    }

    @Override
    public String[] formatUIinfo(String cc, String value) {
        return null;
    }
}
