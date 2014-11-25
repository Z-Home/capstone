package com.pdb.zhome;

import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Devices.Device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brmqk3 on 11/5/2014.
 */
public final class HashMapHelper {

    private static SocketCom socketCom = SocketCom.getInstance();
    private static HashMap<String, Device> deviceHashMap = MainActivity.getHashMap();
    private static String[] allDeviceIds;

    public static ArrayList<String> getDeviceNames(String type){
        ArrayList<String> deviceArrayList = new ArrayList<String>();
        Device[] allDevices = deviceHashMap.values().toArray(new Device[deviceHashMap.size()]);

        for (int i =0; i<allDevices.length; i++){
            if (allDevices[i].getType().equals(type))
                deviceArrayList.add(allDevices[i].getDevNum());
        }

        return  deviceArrayList;
    }

    public static String[] getAllDeviceNames(){

        return deviceHashMap.keySet().toArray(new String[deviceHashMap.size()]);

    }

    public static String[] getAllDeviceIds() {
        return allDeviceIds;
    }

    public static String getStatus(String name){
        Device device = deviceHashMap.get(name);
        String status;
        if (device.getType().equals("switch")){
            status = device.getValues().get("37");
            if (status ==null)
                status = device.getValues().get("38");
        }
        else if(device.getType().equals("sensor")){
            status = device.getValues().toString();
            System.out.println(status);
        }else
            status = null;
        return status;
    }

   public static int getSwitchesOn(){
       int switchesOn=0;
       Device[] allDevices = deviceHashMap.values().toArray(new Device[deviceHashMap.size()]);

       for (int i =0; i<allDevices.length; i++){
           if (allDevices[i].getType().equals("switch"))
               if(getStatus(allDevices[i].getDevNum()).equals("true") || getStatus(allDevices[i].getDevNum()).equals("99"))
                   switchesOn++;
       }
       return switchesOn;
   }

    public static String getType(String name){
        String type = deviceHashMap.get(name).getType();
        return type;
    }

    public static String getName(String key){
        String name = deviceHashMap.get(key).getDevName();
        return name;
    }

    public static String getTemp(String devNum){
        try {
            JSONObject multilevelSensor = new JSONObject(deviceHashMap.get(devNum).getValues().get("49"));
            try{
                JSONObject sensor = multilevelSensor.getJSONObject("sensors");
                return sensor.getString("Temperature");
            } catch (JSONException e){

            }
        }
        catch (JSONException e){

        }
        return "Temp not available";
    }

    public static String getSetTemp(String devNum, String heatOrCool){
        if (heatOrCool.equals("1")){
            try {
                JSONObject setTemp = new JSONObject(deviceHashMap.get(devNum).getValues().get("67"));
                System.out.println(setTemp.getString("heat"));
                return setTemp.getString("heat");
            } catch (JSONException e){

            }
        }else{
            try {
                JSONObject setTemp = new JSONObject(deviceHashMap.get(devNum).getValues().get("67"));
                System.out.println(setTemp.getString("cool"));
                return setTemp.getString("cool");
            } catch (JSONException e){

            }
        }
        return "ERR";
    }

    public static String returnThermostatMode(String devNum){
        String mode = deviceHashMap.get(devNum).getValues().get("66");
        return mode;
    }

    public static void setThermostatMode(String devNum, String mode){
        JSONObject command = deviceHashMap.get(devNum).command("64", mode);
        socketCom.sendMessage(command);
    }

    public static void changeTemp(String devNum, String upOrDown, String heatOrCool){
        String currentSetTemp = "";
        if (heatOrCool.equals("1")){
            try {
                JSONObject setTemp = new JSONObject(deviceHashMap.get(devNum).getValues().get("67"));
                currentSetTemp = setTemp.getString("heat");
            } catch (JSONException e){

            }
        }else{
            try {
                JSONObject setTemp = new JSONObject(deviceHashMap.get(devNum).getValues().get("67"));
                currentSetTemp = setTemp.getString("cool");
            } catch (JSONException e){

            }
        }
        System.out.println("Current temp is: " + currentSetTemp);
        Float oldCelsiusFloatTemp = Float.parseFloat(currentSetTemp);
        if (upOrDown.equals("up"))
            oldCelsiusFloatTemp++;
        else
            oldCelsiusFloatTemp--;
        String changeTemp = heatOrCool + "," + oldCelsiusFloatTemp;
        JSONObject command = deviceHashMap.get(devNum).command("67", changeTemp);
        socketCom.sendMessage(command);
    }
}