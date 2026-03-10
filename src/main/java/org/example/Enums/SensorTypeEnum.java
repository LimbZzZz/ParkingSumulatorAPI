package org.example.Enums;

public enum SensorTypeEnum {
    ENTRY("На въезде"),
    EXIT("На выезде"),
    SPOT("На точке");

    private String sensor;
    SensorTypeEnum(String sensor){
        this.sensor = sensor;
    }

    public String getSensor(){
        return sensor;
    }
}
