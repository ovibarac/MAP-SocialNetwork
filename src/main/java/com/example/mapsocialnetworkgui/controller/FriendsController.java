package com.example.mapsocialnetworkgui.controller;

import com.example.mapsocialnetworkgui.domain.Friendship;
import com.example.mapsocialnetworkgui.domain.FriendshipDTO;
import com.example.mapsocialnetworkgui.domain.FriendshipStatus;
import com.example.mapsocialnetworkgui.domain.User;
import com.example.mapsocialnetworkgui.service.UserService;
import com.example.mapsocialnetworkgui.utils.events.UserEntityChangeEvent;
import com.example.mapsocialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController implements Observer<UserEntityChangeEvent> {
    UserService service;
    User currentUser;
    ObservableList<FriendshipDTO> usersModel = FXCollections.observableArrayList();
    ObservableList<FriendshipDTO> requestsModel = FXCollections.observableArrayList();
    @FXML
    TableView<FriendshipDTO> friendsTable;
    @FXML
    TableColumn<FriendshipDTO, Long> id;
    @FXML
    TableColumn<FriendshipDTO, String> name;
    @FXML
    TableColumn<FriendshipDTO, LocalDateTime> friendsFrom;
    @FXML
    Button addFriendBtn;
    @FXML
    TextField addFriendField;
    @FXML
    Button deleteFriendBtn;
    @FXML
    TableView<FriendshipDTO> requestsTable;
    @FXML
    TableColumn<FriendshipDTO, Long> reqId;
    @FXML
    TableColumn<FriendshipDTO, String> reqName;
    @FXML
    TableColumn<FriendshipDTO, LocalDateTime> reqFriendsFrom;
    @FXML
    Button acceptButton;

    List<Long> sentRequestIds=new ArrayList<>();

    @FXML
    public void initialize(){
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        friendsFrom.setCellValueFactory(new PropertyValueFactory<>("friendsFrom"));
        friendsTable.setItems(usersModel);

        reqId.setCellValueFactory(new PropertyValueFactory<>("id"));
        reqName.setCellValueFactory(new PropertyValueFactory<>("name"));
        reqFriendsFrom.setCellValueFactory(new PropertyValueFactory<>("friendsFrom"));
        requestsTable.setItems(requestsModel);
    }

    public void setUserService(UserService service, User currentUser) {
        this.service = service;
        this.currentUser=currentUser;
        service.addObserver(this);
        initModel();
    }

    private void initModel(){
        usersModel.setAll(friendStatusList(FriendshipStatus.ACCEPTED));
        requestsModel.setAll(friendStatusList(FriendshipStatus.PENDING));
    }

    /**
     * Create a FriendshipDTO list of currentUser's friends with given status
     * @param status - FriendshipStatus: ACCEPTED, PENDING
     * @return List<FriendshipDTO>
     */
    private List<FriendshipDTO> friendStatusList(FriendshipStatus status){
        List<FriendshipDTO> friendships=new ArrayList<>();
        service.allFriendships().forEach(x->{
            if(x!=null){
                if(x.getStatus()== status){
                    if(x.getU1().getId().equals(currentUser.getId())){
                        friendships.add(new FriendshipDTO(x.getId(), x.getU2().getName(), x.getFriendsFrom()));
                    }else if(x.getU2().getId().equals(currentUser.getId())){
                        friendships.add(new FriendshipDTO(x.getId(), x.getU1().getName(), x.getFriendsFrom()));
                    }
                }
            }
        });
        return friendships;
    }

    @Override
    public void update(UserEntityChangeEvent userEntityChangeEvent) {
        initModel();
    }

    public void handleAddFriend() {
        String newFriendName = addFriendField.getText();
        service.findAll().forEach(
                x->{
                    if(x.getName().equals(newFriendName)){
                        try{
                            Friendship friendship = service.addFriendship(0L,currentUser.getId(), x.getId());
                            sentRequestIds.add(x.getId());
                            if(friendship!=null){
                                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Friend request sent!");
                            }else{
                                MessageAlert.showErrorMessage(null, "Cannot send request");
                            }
                        } catch(Exception e){
                            MessageAlert.showErrorMessage(null, e.getMessage());
                        }
                    }
                }
        );
    }

    public void handleDeleteFriend() {
        Long id = friendsTable.getSelectionModel().getSelectedItem().getId();
        try{
            if(service.removeFriendship(id)){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Friend removed!");
                sentRequestIds.remove(id);
            }else{
                MessageAlert.showErrorMessage(null, "Cannot remove friendship");
            }

        } catch(Exception e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleAcceptRequest() {
        Long id = requestsTable.getSelectionModel().getSelectedItem().getId();
        try{
            if(service.updateFriendshipStatus(id, FriendshipStatus.ACCEPTED)==null){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Request accepted!");
            }else{
                MessageAlert.showErrorMessage(null, "Cannot accept request");
            }
        }catch(Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
