package org.example.Enums;

public enum EventTypeEnum {
    ENTRY("Заехал"),
    EXIT("Выехал"),
    RESERVATION("Забронировано"),
    CANCELLATION("Отменено");

    private final String event;

    EventTypeEnum(String event){
        this.event = event;
    }

    public String getEvent(){
        return event;
    }
}
