package org.example.Enums;

public enum StatusEnum {
    FREE("Свободно"),
    OCCUPIED("Занято"),
    RESERVED("Зарезервировано");

    private final String status;

    StatusEnum(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
