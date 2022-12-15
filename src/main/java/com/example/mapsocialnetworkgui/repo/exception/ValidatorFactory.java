package com.example.mapsocialnetworkgui.repo.exception;

public class ValidatorFactory {
    public static Validator createValidator(Strategy strategy){
        if(strategy==Strategy.user){
            return new UserValidator();
        }else {
            return new EntityValidator();
        }
    }
}
;