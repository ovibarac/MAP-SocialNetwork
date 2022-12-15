package com.example.mapsocialnetworkgui.repo.exception;

public class ValidationException extends RuntimeException{
    //RuntimeException ca sa nu fie verificata la compilare, sa se poata propaga pana in UI

    public ValidationException(String message) {
        super(message);
    }
}
