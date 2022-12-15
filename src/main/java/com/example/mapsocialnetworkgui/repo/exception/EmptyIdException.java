package com.example.mapsocialnetworkgui.repo.exception;

public class EmptyIdException extends ValidationException{
    public EmptyIdException() {
        super("Id is empty");
    }
}
