package repo.exception;

public class EmptyNameException extends ValidationException{
    public EmptyNameException() {
        super("Name is empty");
    }
}
