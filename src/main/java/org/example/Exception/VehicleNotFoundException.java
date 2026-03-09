package org.example.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VehicleNotFoundException extends RuntimeException{
    public VehicleNotFoundException(Long id){
        super("Транспортное средство с id " + id + " не найдено");
    }
}
