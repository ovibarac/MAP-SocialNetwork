package repo.exception;

import domain.User;

import java.util.Objects;

public class UserValidator implements Validator<User>{

    @Override
    public void validate(User entity) throws ValidationException {
        if(Objects.equals(entity.getName(), "")) {
            //TODO validation Strategy pattern
            throw new ValidationException("eroare la validare");
        }
    }
}
