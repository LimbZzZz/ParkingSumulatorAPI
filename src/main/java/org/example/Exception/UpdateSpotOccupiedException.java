package org.example.Exception;

public class UpdateSpotOccupiedException extends RuntimeException{
    public UpdateSpotOccupiedException(){
        super("Среди убранных мест есть занятые, обновление не возможно");
    }
}
