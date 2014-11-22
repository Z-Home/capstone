package com.pdb.zhome.Devices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Bryan on 10/13/2014.
 */
public class Switch implements Device {

    private HashMap<String, String> valuesHashMap;
    private String devNum;
    private String type;
    private String devName;

    public Switch(HashMap<String, String> map, String devNum, String devName) {
        this.type = "switch";
        setValues(map);
        setDevNum(devNum);
        setDevName(devName);
    }

    @Override
    public String getType(){return this.type;}

    @Override
    public String getDevNum() {return this.devNum;}

    public void setDevName(String devName) {
        this.devName = devName;
    }

    @Override
    public String getDevName(){
        return this.devName;
    }

    public void setDevNum(String devNum) {
        this.devNum = devNum;
    }

    @Override
    public void setValues(HashMap<String, String> map) {
        this.valuesHashMap = map;
    }

    @Override
    public HashMap<String, String> getValues() {
        return this.valuesHashMap;
    }

    @Override
    public void updateValues(String cc, String value) {
        valuesHashMap.put(cc, value);
    }

    @Override
    public String[] formatUIinfo(String cc, String value){
        updateValues(cc, value);

        String[] values = {this.devNum, cc, value};

        return values;
    }

    @Override
    public JSONObject command(String cc, String value) {
        JSONObject jsonToSend, json;
        jsonToSend = new JSONObject();
        json = new JSONObject();

        if(cc.equals("38")){
            value = value + ",255";
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
}
