package com.example.mapsocialnetworkgui.repo.exception;
import com.example.mapsocialnetworkgui.domain.Entity;
public interface Validator<E extends Entity> {

    public default void validate(E entity) throws ValidationException {

    }
}
