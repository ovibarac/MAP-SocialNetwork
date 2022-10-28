package repo.exception;

import domain.User;

public class UserValidator implements Validator<User>{

    @Override
    public void validate(User entity) throws ValidationException {
        //validare - aici arunca mereu
        throw new ValidationException("eroare la validare");
    }
}
