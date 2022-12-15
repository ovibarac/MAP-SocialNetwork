package com.example.mapsocialnetworkgui.ui;

import com.example.mapsocialnetworkgui.domain.Friendship;
import com.example.mapsocialnetworkgui.domain.User;
import com.example.mapsocialnetworkgui.repo.exception.EmptyIdException;
import com.example.mapsocialnetworkgui.repo.exception.ValidationException;
import com.example.mapsocialnetworkgui.service.UserService;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class Ui {

    UserService srv;
    Scanner scanner = new Scanner(System.in);

    public Ui(UserService srv) {
        this.srv = srv;
    }

    /**
     * Run console UI
     */
    public void run(){
        int option=-1;
        while(option!=0){
            System.out.println("1.Add User\n2.Delete User\n3.Update user\n4.Add friend\n5.Remove friend\n6.Number of communities\n7.Most sociable community\n8.Print Users\n9.Print Friendships\n10.Find user\n11.Find Friendship\n0.Exit");
            System.out.println("Option=");
            option=scanner.nextInt();

            switch (option) {
                case 1 -> addUser();
                case 2 -> deleteUser();
                case 3 -> updateUser();
                case 4 -> addFriend();
                case 5 -> removeFriend();
                case 6 -> nbOfCommunities();
                case 7 -> mostSociable();
                case 8 -> printUsers();
                case 9 -> printFriendships();
                case 10 -> findUser();
                case 11 -> findFriendship();
            }
            System.out.println();
        }

    }

    private void addUser(){
        try{
//            System.out.println("ID=");
            Long id=0L;//scanner.nextLong();
            System.out.println("Name=");
            String name=scanner.next();
            if(srv.createAndAdd(id, name).isEmpty()){
                printUsers();
            }else{
                System.out.println("Id already exists");
            }

        } catch(ValidationException e) {
            System.out.println(e.getMessage());
        }catch(InputMismatchException e){
            System.out.println("Incorrect input");
            scanner.next();
        }
    }

    private void deleteUser(){
        try{
            System.out.println("ID=");
            Long id=scanner.nextLong();
            Optional<User> u = srv.delete(id);
            if(u.isPresent()){
                printUsers();
            }else{
                System.out.println("Id does not exist");
            }

        }catch(InputMismatchException e){
            System.out.println("Incorrect input");
            scanner.next();
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateUser(){
        try{
            System.out.println("ID=");
            Long id=scanner.nextLong();
            System.out.println("Name=");
            String name=scanner.next();
            if(srv.update(id, name).isEmpty()){
                printUsers();
            }else{
                System.out.println("Cannot update");
            }

        } catch(ValidationException e) {
            System.out.println(e.getMessage());
        }catch(InputMismatchException e){
            System.out.println("Incorrect input");
            scanner.next();
        }
    }

    private void addFriend() {
        try{
//            System.out.println("ID Friendship=");
            Long idF=0L;//scanner.nextLong();
            System.out.println("ID1=");
            Long id=scanner.nextLong();
            System.out.println("ID2=");
            Long id2=scanner.nextLong();
            Friendship friendship = srv.addFriendship(idF,id, id2);
            if(friendship!=null){
                System.out.println(friendship);
            }else{
                System.out.println("Cannot add friendship");
            }

        }catch(InputMismatchException e){
            System.out.println("Incorrect input");
            scanner.next();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeFriend() {
        try{
            System.out.println("ID Friendship=");
            Long idF=scanner.nextLong();

            if(srv.removeFriendship(idF)){
                System.out.println("Friendship removed");
            }else{
                System.out.println("Friendship does not exist");
            }

        } catch(InputMismatchException e){
            System.out.println("Incorrect input");
            scanner.next();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void nbOfCommunities(){
        System.out.println("Number of communities: " + srv.nbOfCommunities());
    }

    private void mostSociable(){
        System.out.println("Most sociable community: ");
        srv.mostSociable().forEach(System.out::println);
    }

    private void printUsers(){
        srv.findAll().forEach(System.out::println);
    }

    private void printFriendships(){
        srv.allFriendships().forEach(System.out::println);
    }

    private void findUser(){
        try{
            System.out.println("ID=");
            Long id=scanner.nextLong();
            Optional<User> u = srv.findOne(id);
            if(u.isPresent()){
                System.out.println(u.get());
            }else{
                System.out.println("User does not exist");
            }

        } catch(InputMismatchException e){
            System.out.println("Incorrect input");
            scanner.next();
        }
    }

    private void findFriendship(){
        try{
            System.out.println("ID=");
            Long id=scanner.nextLong();
            Optional<Friendship> f = srv.findFriendship(id);
            if(f.isPresent()){
                System.out.println(f.get());
            }else{
                System.out.println("Friendship does not exist");
            }

        } catch(InputMismatchException e){
            System.out.println("Incorrect input");
            scanner.next();
        }
    }
}
