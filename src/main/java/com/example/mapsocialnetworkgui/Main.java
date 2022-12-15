package com.example.mapsocialnetworkgui;

import com.example.mapsocialnetworkgui.service.UserService;
import com.example.mapsocialnetworkgui.ui.Ui;

public class Main {
    public static void main(String[] args) {
        UserService srv = new UserService();
        Ui ui = new Ui(srv);
        
        ui.run();
    }
}