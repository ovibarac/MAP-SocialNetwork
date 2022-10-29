package service;

import domain.Friendship;
import domain.User;
import repo.InMemoryRepository;
import repo.Repository;
import repo.exception.UserValidator;
import repo.exception.ValidationException;
import repo.exception.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Decorator class and factory for GenericService
 */
public class UserService{

    Service<Long, User> srv;

    public UserService() {
        Validator<User> val = new UserValidator();
        Repository<Long, User> repo = new InMemoryRepository<Long, User>(val);
        this.srv = new GenericService<Long, User>(repo);
    }

    /**
     * create and save new user
     * @param id
     *         id must not be null
     * @param name
     *         name must not be null
     * @return null- if the given id is saved
     *         otherwise returns the entity (id already exists)
     * @throws ValidationException
     *            if the id is not valid
     * @throws ValidationException
     *            if the name is not valid
     */
    public User createAndAdd(Long id, String name){
        User u = new User(id, name);
        return this.srv.add(u);
    }

    public Iterable<User> findAll(){
        return srv.findAll();
    }
    public User delete(Long id){
        return srv.delete(id);
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
    public Friendship addFriendship(Long id1, Long id2){
        User u1 = srv.findOne(id1);
        User u2 = srv.findOne(id2);
        if(u1==u2 || u1 == null || u2 == null){
            return null;
        }
        Friendship friendship = new Friendship(u1, u2);
        ArrayList<Friendship> friendships1 = u1.getFriendships();
        ArrayList<Friendship> friendships2 = u2.getFriendships();
        if(friendships1.contains(friendship)){
            return null;
        }else{
            friendships1.add(friendship);
            friendships2.add(friendship);
        }

        return friendship;
    }

    /**
     * removes friendship between entities with specified id
     * @param id1 - id of first entity
     *      id must be not null
     * @param id2 - id of first entity
     *      id must be not null
     * @return false if Friendship doesn't exist or id1==id2
     *         or true
     */
    public boolean removeFriendship(Long id1, Long id2){
        User u1 = srv.findOne(id1);
        User u2 = srv.findOne(id2);
        if(u1==u2 || u1 == null || u2 == null){
            return false;
        }
        Friendship friendship = new Friendship(u1, u2);
        ArrayList<Friendship> friendships1 = u1.getFriendships();
        ArrayList<Friendship> friendships2 = u2.getFriendships();
        return friendships1.remove(friendship) && friendships2.remove(friendship);
    }

    /**
     * @return the number of connected components
     */
    public AtomicInteger nbOfCommunities(){
        //dfs
        Map<Long, Boolean> viz = new HashMap<>();
        srv.findAll().forEach((x)->{viz.put(x.getId(), false);});
        AtomicInteger componentCount = new AtomicInteger();
        srv.findAll().forEach(
                (x)->{
                    if(!viz.get(x.getId())){
                        User crt = srv.findOne(x.getId());
                        DFS(viz, crt);
                        componentCount.getAndIncrement();
                    }
                }
        );
        return componentCount;
    }

    private void DFS(Map<Long, Boolean> viz, User crtUser){
        viz.remove(crtUser.getId());
        viz.put(crtUser.getId(), true);
        crtUser.getFriendships().forEach((x)->{
        //TODO make map change outside of function
            if(x.getU1() == crtUser && !viz.get(x.getU1().getId())){
                DFS(viz, x.getU2());
            }else if(x.getU2() == crtUser && !viz.get(x.getU2().getId())){
                DFS(viz, x.getU1());
            }
        });
    }
}
