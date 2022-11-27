package repo;

import domain.User;
import repo.exception.Validator;

import java.io.*;
import java.util.Optional;

public class UserFileRepository extends AbstractFileRepository<Long, User>{

    public UserFileRepository(Validator<User> validator, String userFilename) {
        super(validator, userFilename);
        loadFromFile();
    }

    @Override
    void loadFromFile(){
        try{
            BufferedReader reader= new BufferedReader(new FileReader(file.getName()));
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
}
