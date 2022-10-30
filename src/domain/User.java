package domain;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class User extends Entity<Long>{
    private String name;

    private ArrayList<Friendship> friendships;

    public User(Long id, String name) {
        super(id);
        this.name = name;
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
