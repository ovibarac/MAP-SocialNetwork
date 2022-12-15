package com.example.mapsocialnetworkgui.utils.observer;


import com.example.mapsocialnetworkgui.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}