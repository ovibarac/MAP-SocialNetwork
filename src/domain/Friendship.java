package domain;

import java.util.Objects;

public class Friendship {
    User u1;
    User u2;

    public Friendship(User u1, User u2) {
        this.u1 = u1;
        this.u2 = u2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(u1, that.u1) && Objects.equals(u2, that.u2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(u1, u2);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "u1=" + u1 +
                ", u2=" + u2 +
                '}';
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
}
