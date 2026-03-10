package org.example.Exception;

public class SensorNotFoundException extends RuntimeException{
    public SensorNotFoundException(Long id){
        super("Сенсора с id " + id + " не существует");
    }
}
