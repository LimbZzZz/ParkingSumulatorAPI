package org.example.Exception;

public class SpotNotFoundException extends RuntimeException{
    public SpotNotFoundException(Long id){
        super("Место с id " + id + " не найдено");
    }
}
