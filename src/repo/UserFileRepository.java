package repo;

import domain.User;
import repo.exception.Validator;

import java.io.*;

public class UserFileRepository extends InMemoryRepository<Long, User>{
    File userFile;

    public UserFileRepository(Validator validator, String userFilename) {
        super(validator);
        userFile=new File(userFilename);
        try {
            userFile.createNewFile();
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        loadFromFile();
    }

    private void loadFromFile(){
        try{
            BufferedReader reader= new BufferedReader(new FileReader(userFile.getName()));
            String line= reader.readLine();
            while(line!=null){
                save(new User(line));
                line= reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToFile(){
        try {
            FileWriter writer = new FileWriter(userFile.getName());
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

    @Override
    public User save(User entity) {
        User e = super.save(entity);
        saveToFile();
        return e;
    }

    @Override
    public User delete(Long id) {
        User d = super.delete(id);
        saveToFile();
        return d;
    }


}
