package ui;

import domain.User;
import repo.exception.ValidationException;
import service.UserService;

public class Ui {

    UserService srv;

    public Ui(UserService srv) {
        this.srv = srv;
    }

    public void run(){
        try {
            srv.createAndAdd(1L, "Mimi");
            srv.createAndAdd(2L, "Mumu");
            srv.createAndAdd(3L, "Mama");
        }catch(ValidationException | IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        //repo.findAll().forEach(x-> System.out.println(x));
        srv.findAll().forEach(System.out::println);

//        try{
//            srv.delete(1L);
//        }catch (IllegalArgumentException e){
//            System.out.println(e.getMessage());
//        }
        srv.findAll().forEach((x)->{
            x.getFriendships().forEach(System.out::println);});

        System.out.println(srv.addFriendship(1L, 2L));
        System.out.println(srv.addFriendship(1L, 3L));
        System.out.println(srv.addFriendship(2L, 3L));
        System.out.println(srv.removeFriendship(2L, 3L));
        System.out.println(srv.removeFriendship(2L, 3L));

        srv.findAll().forEach((x)->{
            x.getFriendships().forEach(System.out::println);});

    }
}
