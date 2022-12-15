package com.example.mapsocialnetworkgui.controller;

import com.example.mapsocialnetworkgui.UserApplication;
import com.example.mapsocialnetworkgui.domain.User;
import com.example.mapsocialnetworkgui.repo.exception.ValidationException;
import com.example.mapsocialnetworkgui.service.UserService;
import com.example.mapsocialnetworkgui.utils.events.UserEntityChangeEvent;
import com.example.mapsocialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserEntityChangeEvent> {
    UserService service;

    ObservableList<User> usersModel = FXCollections.observableArrayList();
    @FXML
    TableView<User> userTable;
    @FXML
    TableColumn<User, Long> id;
    @FXML
    TableColumn<User, String> name;
    @FXML
    Button addUserBtn;
    @FXML
    TextField addUserField;
    @FXML
    Button deleteUserBtn;
    @FXML
    Button updateUserBtn;
    @FXML
    Button friendsButton;

    @FXML
    public void initialize(){
        id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        userTable.setItems(usersModel);
    }

    public void setUserService(UserService service) {
        this.service = service;
        service.addObserver(this);
        initModel();
    }

    private void initModel(){
        Iterable<User> users =service.findAll();
        List<User> usersList = StreamSupport.stream(users.spliterator(), false).toList();
        usersModel.setAll(usersList);
    }

    @Override
    public void update(UserEntityChangeEvent userEntityChangeEvent) {
        initModel();
    }

    public void handleAddUser(){
        String newName = addUserField.getText();
        try{
            if(service.createAndAdd(0L, newName).isEmpty()){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Successfully added user!");
            }else{
                MessageAlert.showErrorMessage(null, "Id already exists");
            }
        } catch(ValidationException | InputMismatchException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleDeleteUser(){
        Long id = userTable.getSelectionModel().getSelectedItem().getId();
        try{
            Optional<User> u = service.delete(id);
            if(u.isPresent()){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Successfully deleted user!");
            }else{
                MessageAlert.showErrorMessage(null, "Id does not exist");
            }
        } catch(Exception e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleUpdateUser() {
        String newName = addUserField.getText();
        Long id = userTable.getSelectionModel().getSelectedItem().getId();
        try{
            if(service.update(id, newName).isEmpty()){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Successfully updated user!");
            }else{
                MessageAlert.showErrorMessage(null, "Cannot update");
            }

        } catch(ValidationException | InputMismatchException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleFriendsWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(UserApplication.class.getResource("friends-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();

            User currentUser = userTable.getSelectionModel().getSelectedItem();

            FriendsController friendsController = fxmlLoader.getController();
            friendsController.setUserService(service, currentUser);

            stage.setTitle(currentUser.getName() + "'s friends");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            MessageAlert.showErrorMessage(null, "Failed to create new Window");
        }
    }
}
