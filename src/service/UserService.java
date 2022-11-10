package service;

import domain.Friendship;
import domain.User;
import repo.FriendshipFileRepo;
import repo.UserFileRepository;
import repo.Repository;
import repo.exception.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Decorator class and factory for GenericService
 */
public class UserService{

    Service<Long, User> srv;
    FriendshipFileRepo friendshipRepo;
    public UserService() {
        Validator<User> val = ValidatorFactory.createValidator(Strategy.user);
//        Repository<Long, User> repo = new InMemoryRepository<Long, User>(val);
        Repository<Long, User> repo = new UserFileRepository(val, "users.txt");
        friendshipRepo = new FriendshipFileRepo("friendships.txt",repo);
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
     *            or if the name is not valid
     */
    public User createAndAdd(Long id, String name){
        User u = new User(id, name);
        return this.srv.add(u);
    }

    /**
     * @return all users
     */
    public Iterable<User> findAll(){
        return srv.findAll();
    }

    /**
     * @return all friendships
     */
    public List<Friendship> allFriendships(){
        return friendshipRepo.getFriendships();
    }

    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
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
            friendshipRepo.save(friendship);
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
        return friendships1.remove(friendship) && friendships2.remove(friendship) && friendshipRepo.remove(friendship);
    }

    /**
     * @return the number of connected components
     */
    public AtomicInteger nbOfCommunities(){
        Map<Long, Boolean> viz = new HashMap<>();
        srv.findAll().forEach((x)->{viz.put(x.getId(), false);});
        AtomicInteger componentCount = new AtomicInteger();
        srv.findAll().forEach(
                (x)->{
                    if(!viz.get(x.getId())){
                        User crt = srv.findOne(x.getId());
                        DFSDist(viz, crt);
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
        //TODO make it count 1-2 1-3 as one community
        srv.findAll().forEach((x)->{viz.put(x.getId(), false);});
        int maxL = 0;
        Long idNode=0L;
        for (User x:srv.findAll()) {
            if(!viz.get(x.getId())){
                User crt = srv.findOne(x.getId());
                int dist=DFSDist(viz, crt);
                if(maxL<dist){
                    maxL=dist;
                    idNode=crt.getId();
                }
            }
        }

        Map<Long, Boolean> viz2 = new HashMap<>();
        srv.findAll().forEach((x)->{viz2.put(x.getId(), false);});

        ArrayList<User> list=new ArrayList<>();
        DFSList(viz2, srv.findOne(idNode), list);
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
}
