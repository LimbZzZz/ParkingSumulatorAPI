package org.example.Enums;

public enum SpotTypeEnum {
    REGULAR("Обычное"),
    DISABLED("Для инвалидов"),
    ELECTRIC("Для электро-каров"),
    VIP("Платное");

    private final String spot;

    SpotTypeEnum(String spot){
        this.spot = spot;
    }

    public String getSpot(){
        return spot;
    }
}
