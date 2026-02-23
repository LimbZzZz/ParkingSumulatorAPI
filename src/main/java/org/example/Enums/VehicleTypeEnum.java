package org.example.Enums;

public enum VehicleTypeEnum {
    CAR("Автомобиль"),
    MOTORCYCLE("Мотоцикл"),
    TRUCK("Грузовик");

    private final String type;

    VehicleTypeEnum(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
