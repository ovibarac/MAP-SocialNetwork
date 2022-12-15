package com.example.mapsocialnetworkgui.repo.exception;

import com.example.mapsocialnetworkgui.domain.User;

import java.util.Objects;

public class UserValidator implements Validator<User>{

    @Override
    public void validate(User entity) throws ValidationException {
        if(Objects.equals(entity.getName(), "")) {
            throw new EmptyNameException();
        }

        if(Objects.equals(entity.getId(), null)){
            throw new EmptyIdException();
        }

        if(entity.getName().matches(".*[0-9]+.*")){
            throw new NameContainsNumbersException();
        }
    }
}
