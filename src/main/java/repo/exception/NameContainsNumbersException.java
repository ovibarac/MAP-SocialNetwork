package repo.exception;

public class NameContainsNumbersException extends ValidationException{
    public NameContainsNumbersException() {
        super("Name contains numbers");
    }
}
