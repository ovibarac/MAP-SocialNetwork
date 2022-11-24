package repo.exception;
import domain.Entity;
public interface Validator<E extends Entity> {

    public default void validate(E entity) throws ValidationException {

    }
}
