package repo;

import domain.Friendship;
import domain.User;
import repo.exception.EntityValidator;
import repo.exception.Validator;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipFileRepo extends AbstractFileRepository<Long, Friendship> {

    Repository<Long, User> userRepo;
    public FriendshipFileRepo(Validator<Friendship> validator, String filename, Repository<Long, User> userRepo) {
        super(validator, filename);
        this.userRepo=userRepo;
        loadFromFile();
    }

    @Override
    void loadFromFile(){
        try{
            BufferedReader reader= new BufferedReader(new FileReader(file.getName()));
            String line= reader.readLine();
            while(line!=null){
                Long idF=Long.parseLong(line.substring(line.indexOf('{') + 1, line.indexOf(':')));
                Long id1=Long.parseLong(line.substring(line.indexOf(':') + 1, line.indexOf(',')));
                Long id2=Long.parseLong(line.substring(line.indexOf(',') + 1, line.indexOf(' ')-1));
//                LocalDateTime date = LocalDateTime.parse(line.substring(line.indexOf(' ')+1, line.indexOf('}')));

                Friendship f = createFriendshipAndAddToUsers(idF, id1, id2);

                save(f);
                line= reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void saveToFile(){
        try {
            FileWriter writer = new FileWriter(file.getName());
            entities.forEach((id, ent)->{
                try {
                    writer.write(ent.toString()+'\n');
                } catch (IOException ignored) {
                }
            });
            writer.close();
        }catch(IOException ignored){
        }
    }

    /**
     * Create new friendship and add to users frienship lists
     * @param id - id of new friendship, does nothing in DB
     * @param idU1 - id of first user
     * @param idU2 - id of second user
     * @return null if users are not found,
     *         or the new Friendship
     */
    public Friendship createFriendshipAndAddToUsers(Long id, Long idU1, Long idU2){
        if(userRepo.findOne(idU1).isEmpty() || userRepo.findOne(idU2).isEmpty())
            return null;
        User u1 = userRepo.findOne(idU1).get();
        User u2 = userRepo.findOne(idU2).get();

        Friendship f = new Friendship(id,u1, u2);//, localDateTime);

        ArrayList<Friendship> friendships1 = u1.getFriendships();
        friendships1.add(f);
        u1.setFriendships(friendships1);

        ArrayList<Friendship> friendships2 = u2.getFriendships();
        friendships2.add(f);
        u2.setFriendships(friendships2);

        return f;
    }
}
