package com.example.mapsocialnetworkgui.service;

import com.example.mapsocialnetworkgui.domain.Friendship;
import com.example.mapsocialnetworkgui.domain.FriendshipStatus;
import com.example.mapsocialnetworkgui.domain.User;
import com.example.mapsocialnetworkgui.repo.*;
import com.example.mapsocialnetworkgui.repo.exception.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import com.example.mapsocialnetworkgui.repo.exception.Validator;
import com.example.mapsocialnetworkgui.repo.exception.UserValidator;
import com.example.mapsocialnetworkgui.utils.events.ChangeEventType;
import com.example.mapsocialnetworkgui.utils.events.UserEntityChangeEvent;
import com.example.mapsocialnetworkgui.utils.observer.Observable;
import com.example.mapsocialnetworkgui.utils.observer.Observer;

public class UserService implements Observable<UserEntityChangeEvent> {

    Repository<Long, Friendship> friendshipRepo;

    Repository<Long, User> repo;

    private List<Observer<UserEntityChangeEvent>> observers=new ArrayList<>();

    public UserService() {
        Validator<User> val = ValidatorFactory.createValidator(Strategy.user);
        Validator<Friendship> valF = ValidatorFactory.createValidator(Strategy.entity);
//        Repository<Long, User> repo = new InMemoryRepository<Long, User>(val);
//        repo = new UserFileRepository(val, "src/users.txt");
//        friendshipRepo = new FriendshipFileRepo(valF, "src/friendships.txt",repo);
        repo = new UserDbRepo("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres", (UserValidator) val);
        friendshipRepo = new FriendshipDbRepo("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres", valF, repo);
    }

    /**
     * create and save new user
     * @param id
     *         id must not be null
     * @param name
     *         name must not be null
     * @return Optional<User> containing
     *         null- if the given id is saved
     *         otherwise returns the entity (id already exists)
     * @throws ValidationException
     *            if the id is not valid
     *            or if the name is not valid
     */
    public Optional<User> createAndAdd(Long id, String name){
        User u = new User(id, name);
        Optional<User> output = repo.save(u);
        notifyObservers(new UserEntityChangeEvent(ChangeEventType.ADD, u));
        return output;
    }

    /**
     * @return all users
     */
    public Iterable<User> findAll(){
        return repo.findAll();
    }

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return Optional<User> containing
     *          the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    public Optional<User> findOne(Long id){return repo.findOne(id);}

    /**
     * @return all friendships
     */
    public Iterable<Friendship> allFriendships(){
        return friendshipRepo.findAll();
    }

    public Optional<Friendship> findFriendship(Long id){
        return friendshipRepo.findOne(id);
    }

    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return Optional<User> containing the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    public Optional<User> delete(Long id){
        Optional<User> output= repo.delete(id);
        output.ifPresent(user -> notifyObservers(new UserEntityChangeEvent(ChangeEventType.DELETE, user)));
        return output;
    }

    /**
     * update entity
     * @param id - id of existing User
     * @param name - new name of User
     * @return Optional<User> containing null - if the entity is updated,
     *                otherwise the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */
    public Optional<User> update(Long id, String name){
        User u = new User(id, name);
        Optional<User> output = repo.update(u);
        if(output.isEmpty()){
            notifyObservers(new UserEntityChangeEvent(ChangeEventType.UPDATE, u));
        }
        return output;
    }

    /**
     * creates and adds friendship between entities with specified id
     * @param id1 - id of first entity
     *      id must be not null
     * @param id2 - id of first entity
     *      id must be not null
     * @return null if Friendship exists or id1==id2
     *         or the new Friendship
     */
    public Friendship addFriendship(Long idFriendship, Long id1, Long id2){
        Optional<User> u1 = repo.findOne(id1);
        Optional<User> u2 = repo.findOne(id2);

        if(u1==u2 || u1.isEmpty() || u2.isEmpty()){
            return null;
        }
        //TODO move this code in friendship repo save
        Friendship friendship = new Friendship(idFriendship, u1.get(), u2.get(), LocalDateTime.now(), FriendshipStatus.PENDING);
        ArrayList<Friendship> friendships1 = u1.get().getFriendships();
        ArrayList<Friendship> friendships2 = u2.get().getFriendships();
        if(friendships1.contains(friendship) || friendships2.contains(friendship)){
            return null;
        }else{
            friendships1.add(friendship);
            friendships2.add(friendship);
            friendshipRepo.save(friendship);
            notifyObservers(new UserEntityChangeEvent(ChangeEventType.ADD, null));
        }

        return friendship;
    }

    /**
     * updates friendship status and date
     * @param idFriendship - id of first friendship
     *      id must be not null
     * @param newStatus - new status of friendship
     *      ACCEPTED or PENDING
     * @return Optional<E> containing
     *       null - if the entity is updated,
     *       otherwise the entity  - (e.g id does not exist).
     */
    public Friendship updateFriendshipStatus(Long idFriendship, FriendshipStatus newStatus){
        Optional<Friendship> friendship = findFriendship(idFriendship);
        if(friendship.isPresent()){
            friendship.get().setFriendsFrom(LocalDateTime.now());
            friendship.get().setStatus(newStatus);
            Optional<Friendship> output = friendshipRepo.update(friendship.get());
            if(output.isEmpty()){
                notifyObservers(new UserEntityChangeEvent(ChangeEventType.ADD, null));
                return null;
            }
        }
        return friendship.get();
    }

    /**
     * removes friendship between entities with specified id
     * @param idFriendship - id of Friendship
     *      id must be not null
     * @return false if Friendship doesn't exist
     *         or true
     */
    public boolean removeFriendship(Long idFriendship){
        Optional<Friendship> friendship = friendshipRepo.findOne(idFriendship);
        if(friendship.isEmpty())
            return false;
        User u1 = friendship.get().getU1();
        User u2 = friendship.get().getU2();
        if(u1==u2 || u1 == null || u2 == null){
            return false;
        }
        ArrayList<Friendship> friendships1 = u1.getFriendships();
        ArrayList<Friendship> friendships2 = u2.getFriendships();
        boolean removed= friendshipRepo.delete(idFriendship).isPresent() && friendships1.remove(friendship.get()) && friendships2.remove(friendship.get());
        if(removed){
            notifyObservers(new UserEntityChangeEvent(ChangeEventType.DELETE, null));
        }
        return removed;
    }

    /**
     * @return the number of connected components
     */
    public AtomicInteger nbOfCommunities(){
        Map<Long, Boolean> viz = new HashMap<>();
        repo.findAll().forEach((x)->{viz.put(x.getId(), false);});
        AtomicInteger componentCount = new AtomicInteger();
        repo.findAll().forEach(
                (x)->{
                    if(!viz.get(x.getId())){
                        Optional<User> crt = repo.findOne(x.getId());
                        crt.ifPresent(user -> DFSDist(viz, user));
                        componentCount.getAndIncrement();
                    }
                }
        );
        return componentCount;
    }

    /**
     * find longest connected component
     * @return list of users in most connected component
     */
    public ArrayList<User> mostSociable(){
        Map<Long, Boolean> viz = new HashMap<>();
        repo.findAll().forEach((x)->{viz.put(x.getId(), false);});
        int maxL = 0;
        Long idNode=0L;
        for (User x:repo.findAll()) {
            Optional<User> crt = repo.findOne(x.getId());
            if(crt.isPresent()){
                int dist=DFSDist(viz, crt.get());
                if(maxL<dist){
                    maxL=dist;
                    idNode=crt.get().getId();
                }
            }
        }

        Map<Long, Boolean> viz2 = new HashMap<>();
        repo.findAll().forEach((x)->{viz2.put(x.getId(), false);});

        ArrayList<User> list=new ArrayList<>();
        DFSList(viz2, repo.findOne(idNode).get(), list);
        return list;
    }

    /**
     * depth-first search in user network and calculate distance
     * @param viz     - vizited users
     * @param crtUser - current node
     * @return distance of current path
     */
    private int DFSDist(Map<Long, Boolean> viz, User crtUser){
        viz.remove(crtUser.getId());
        viz.put(crtUser.getId(), true);

        for (Friendship friendship: crtUser.getFriendships()) {
            if(friendship.getU1() == crtUser && !viz.get(friendship.getU2().getId())){
                return 1+DFSDist(viz, friendship.getU2());
            }else if(friendship.getU2() == crtUser && !viz.get(friendship.getU1().getId())){
                return 1+DFSDist(viz, friendship.getU1());
            }
        }

        return 1;
    }

    /**
     * depth-first search in user network and save visited nodes
     * @param viz     - vizited users
     * @param crtUser - current node
     * @param list - output list to save visited nodes
     */
    private void DFSList(Map<Long, Boolean> viz, User crtUser, ArrayList<User> list){
        viz.remove(crtUser.getId());
        viz.put(crtUser.getId(), true);
        list.add(crtUser);
        for (Friendship friendship: crtUser.getFriendships()) {
            if(friendship.getU1() == crtUser && !viz.get(friendship.getU2().getId())){
                DFSList(viz, friendship.getU2(), list);
            }else if(friendship.getU2() == crtUser && !viz.get(friendship.getU1().getId())){
                DFSList(viz, friendship.getU1(), list);
            }
        }
    }

    @Override
    public void addObserver(Observer<UserEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEntityChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
}
