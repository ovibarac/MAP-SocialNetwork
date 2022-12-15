package com.example.mapsocialnetworkgui;

import com.example.mapsocialnetworkgui.controller.UserController;
import com.example.mapsocialnetworkgui.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        UserService service = new UserService();

        FXMLLoader fxmlLoader = new FXMLLoader(UserApplication.class.getResource("user-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        UserController controller=fxmlLoader.getController();
        controller.setUserService(service);

        stage.setTitle("Users");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
