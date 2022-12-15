package com.example.mapsocialnetworkgui.utils.observer;


import com.example.mapsocialnetworkgui.utils.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
