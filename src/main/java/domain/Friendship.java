package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

public class Friendship extends Entity<Long>{
    User u1;
    User u2;

    LocalDateTime friendsFrom;

    public Friendship(Long id, User u1, User u2) {
        super(id);
        this.u1 = u1;
        this.u2 = u2;
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        friendsFrom= LocalDateTime.now();
    }

    public Friendship(Long id, User u1, User u2, LocalDateTime friendsFrom) {
        super(id);
        this.u1 = u1;
        this.u2 = u2;
        this.friendsFrom=friendsFrom;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(u1, that.u1) && Objects.equals(u2, that.u2) ||
                Objects.equals(u1, that.u2) && Objects.equals(u2, that.u1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(u1, u2);
    }

    @Override
    public String toString() {
        return "Friendship{" + this.getId()+':' + u1.getId() + ','+u2.getId()+", "+ friendsFrom.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +'}';
    }

    public User getU1() {
        return u1;
    }

    public void setU1(User u1) {
        this.u1 = u1;
    }

    public User getU2() {
        return u2;
    }

    public void setU2(User u2) {
        this.u2 = u2;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }
}
