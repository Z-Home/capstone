package com.pdb.zhome;

import com.miz.pdb.R;

import java.util.HashMap;

/**
 * Created by broccard on 11/22/14.
 */
public class Room{

    public enum RoomType{
        BEDROOM,
        OFFICE,
        BATHROOM,
        KITCHEN,
        DINING,
        LIVINGROOM,
        OUTSIDE,
        OTHER
    };

    private static HashMap<String, RoomType> stringToTypeHashMap;

    private String name;
    private String[] devices;
    private RoomType type;

    static {
        stringToTypeHashMap = new HashMap<String, RoomType>();
        stringToTypeHashMap.put("Bedroom", RoomType.BEDROOM);
        stringToTypeHashMap.put("Office", RoomType.OFFICE);
        stringToTypeHashMap.put("Bathroom", RoomType.BATHROOM);
        stringToTypeHashMap.put("Kitchen", RoomType.KITCHEN);
        stringToTypeHashMap.put("Dining", RoomType.DINING);
        stringToTypeHashMap.put("Living Room", RoomType.LIVINGROOM);
        stringToTypeHashMap.put("Outside", RoomType.OUTSIDE);
        stringToTypeHashMap.put("Other", RoomType.OTHER);
    }

    public Room(String name, String[] devices, RoomType type){
        this.name = name;
        this.devices = devices;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getDevices() {
        return devices;
    }

    public void setDevices(String[] devices) {
        this.devices = devices;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public static RoomType getTypeGivenString(String roomTypeString) {
        System.out.println(roomTypeString);
        System.out.println(stringToTypeHashMap.size());
        return stringToTypeHashMap.get(roomTypeString);
    }

    public String getStringType() {

        if(getType() == Room.RoomType.BATHROOM){
            return "Bathroom";
        }else if(getType() == Room.RoomType.BEDROOM){
            return "Bedroom";
        }else if(getType() == Room.RoomType.DINING){
            return "Dining";
        }else if(getType() == Room.RoomType.KITCHEN){
            return "Kitchen";
        }else if(getType() == Room.RoomType.LIVINGROOM){
            return "Living Room";
        }else if(getType() == Room.RoomType.OFFICE){
            return "Office";
        }else if(getType() == Room.RoomType.OUTSIDE){
           return "Outside";
        }else{
            return "Other";
        }
    }

}
