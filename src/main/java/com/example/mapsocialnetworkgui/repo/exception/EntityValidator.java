package com.example.mapsocialnetworkgui.repo.exception;

import com.example.mapsocialnetworkgui.domain.Entity;
import com.example.mapsocialnetworkgui.domain.User;

import java.util.Objects;

public class EntityValidator implements Validator<Entity>{

    @Override
    public void validate(Entity entity) throws ValidationException {
        if(Objects.equals(entity.getId(), "")){
            throw new EmptyIdException();
        }
    }
}