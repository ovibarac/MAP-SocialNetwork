package repo;

import domain.Friendship;
import domain.User;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipFileRepo {
    File file;
    List<Friendship> friendships;
    Repository<Long, User> userRepo;
    public FriendshipFileRepo(String filename, Repository userRepo) {
        this.file = new File(filename);
        this.userRepo=userRepo;
        file=new File(filename);
        friendships=new ArrayList<>();
        try {
            file.createNewFile();
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        loadFromFile();

    }

    public List<Friendship> getFriendships() {
        return friendships;
    }

    private void loadFromFile(){
        try{
            BufferedReader reader= new BufferedReader(new FileReader(file.getName()));
            String line= reader.readLine();
            while(line!=null){
                Long id1=Long.parseLong(line.substring(line.indexOf('{') + 1, line.indexOf(',')));
                Long id2=Long.parseLong(line.substring(line.indexOf(',') + 1, line.indexOf(' ')-1));
                LocalDateTime date = LocalDateTime.parse(line.substring(line.indexOf(' ')+1, line.indexOf('}')));
                User u1 = userRepo.findOne(id1);
                User u2 = userRepo.findOne(id2);

                Friendship f = new Friendship(u1, u2, date);

                ArrayList<Friendship> friendships1 = u1.getFriendships();
                friendships1.add(f);
                u1.setFriendships(friendships1);

                ArrayList<Friendship> friendships2 = u2.getFriendships();
                friendships2.add(f);
                u2.setFriendships(friendships2);

                save(f);
                line= reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToFile(){
        try {
            FileWriter writer = new FileWriter(file.getName());
            friendships.forEach((ent)->{
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
     * Add new friendship to repo
     * @param friendship- the new friendship
     * @return the added friendship
     */
    public Friendship save(Friendship friendship) {
        if (friendship==null)
            throw new IllegalArgumentException("entity must be not null");

        friendships.add(friendship);
        saveToFile();
        return friendship;
    }

    /**
     * Removes specified friendship from repo
     * @param friendship- Friendship to be removed
     * @return true if successful, false if not
     */
    public boolean remove(Friendship friendship){
        boolean removed =friendships.remove(friendship);
        saveToFile();
        return removed;
    }
}
