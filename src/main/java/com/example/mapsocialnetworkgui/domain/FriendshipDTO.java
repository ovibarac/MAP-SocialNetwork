package com.example.mapsocialnetworkgui.domain;

import java.time.LocalDateTime;

public class FriendshipDTO extends Entity<Long>{

    private String name;
    private LocalDateTime friendsFrom;

    public FriendshipDTO(Long aLong, String name, LocalDateTime friendsFrom) {
        super(aLong);
        this.name=name;
        this.friendsFrom=friendsFrom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }
}
