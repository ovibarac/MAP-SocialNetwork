package repo.exception;

import domain.Entity;
import domain.User;

import java.util.Objects;

public class EntityValidator implements Validator<Entity>{

    @Override
    public void validate(Entity entity) throws ValidationException {
        if(Objects.equals(entity.getId(), "")){
            throw new EmptyIdException();
        }
    }
}