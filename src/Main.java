import service.UserService;
import ui.Ui;

public class Main {
    public static void main(String[] args) {
        UserService srv = new UserService();
        Ui ui = new Ui(srv);
        
        ui.run();
    }
}