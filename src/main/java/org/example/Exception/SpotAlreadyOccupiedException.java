package org.example.Exception;

public class SpotAlreadyOccupiedException extends RuntimeException{
    public SpotAlreadyOccupiedException(Integer number){
        super("Место " + number  + " уже занято");
    }
}
