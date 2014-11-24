package com.pdb.zhome.Devices;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Bryan on 10/13/2014.
 */
public class Sensor implements Device {

    private String type;
    private String devNum;
    private String devName;
    private HashMap<String, String> values;

    public Sensor(HashMap<String, String> map, String devNum, String devName) {
        setDevNum(devNum);
        setDevName(devName);
        setValues(map);
        this.type = "sensor";
    }

    @Override
    public String getType(){return this.type;}

    @Override
    public String getDevName() {
        return this.devName;
    }

    @Override
    public String getDevNum() {return this.devNum;}

    public void setDevNum(String devNum) {
        this.devNum = devNum;
    }

    public void setDevName(String devName) {

        this.devName = devName;
    }

    @Override
    public void setValues(HashMap<String, String> map) {
//        System.out.println("------------------ "+map+" ---------------------");
//        Iterator<String> keys = map.keySet().iterator();
//        while(keys.hasNext()){
//            if (keys.next().equals("48")){
//                System.out.println(map.get("48"));
//            } else if (keys.next().equals("49")){
//                System.out.println(map.get("49"));
//            }
//        }
        values = map;
    }

    @Override
    public HashMap<String, String> getValues() {
        return values;
    }

    @Override
    public void updateValues(String cc, String value) {
        values.put(cc, value);
    }

    @Override
    public JSONObject command(String cc, String value) {
        return null;
    }

    @Override
    public String[] formatUIinfo(String cc, String value) {
        updateValues(cc, value);
        return null;
    }
}
