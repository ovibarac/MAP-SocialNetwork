import domain.Entity;
import domain.User;
import repo.InMemoryRepository;
import repo.Repository;
import repo.exception.UserValidator;
import repo.exception.ValidationException;
import repo.exception.Validator;
import service.Service;
import service.UserService;
import ui.Ui;

public class Main {
    public static void main(String[] args) {
        UserService srv = new UserService();
        Ui ui = new Ui(srv);
        
        ui.run();
    }
}