package org.example.Exception;

public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException(Long id){
        super("Событие " + id + " не найдено");
    }
}
