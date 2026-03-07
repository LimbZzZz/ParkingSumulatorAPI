package org.example.Exception;

public class ParkingNotFoundException extends RuntimeException{
    public ParkingNotFoundException(Long id){
        super("Парковка с id " + id + " не найдена");
    }
}
