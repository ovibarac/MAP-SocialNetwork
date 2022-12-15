package com.example.mapsocialnetworkgui.domain;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import com.example.mapsocialnetworkgui.domain.Entity;
import com.example.mapsocialnetworkgui.domain.Friendship;

public class User extends Entity<Long> {
    private String name;

    private ArrayList<Friendship> friendships;

    public User(Long id, String name) {
        super(id);
        this.name = name;
        this.friendships=new ArrayList<Friendship>();
    }

    /**
     * Create instance from string
     * @param userString - file string, written as Name{id=x}
     */
    public User(String userString){
//        super(userString);
        super(Long.parseLong(userString.substring(userString.indexOf('=') + 1, userString.indexOf('}'))));
        this.name=userString.substring(0, userString.indexOf('{'));
        this.friendships=new ArrayList<Friendship>();
    }

    public ArrayList<Friendship> getFriendships() {
        return friendships;
    }

    public void setFriendships(ArrayList<Friendship> friendships) {
        this.friendships = friendships;
   }

    @Override
    public String toString() {
        return name + "{" + "id=" + this.getId().toString() +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
