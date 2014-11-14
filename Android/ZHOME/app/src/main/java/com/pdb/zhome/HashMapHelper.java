package com.pdb.zhome;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brmqk3 on 11/5/2014.
 */
public final class HashMapHelper {

    private static HashMap<String, Device> deviceHashMap = MainActivity.getHashMap();

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

    public static String getStatus(String name){
        Device device = deviceHashMap.get(name);
        String status;
        if (device.getType().equals("switch")){
            status = device.getValues().get("37");
            if (status ==null)
                status = device.getValues().get("38");
        }
        else
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

}