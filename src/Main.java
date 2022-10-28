import domain.Entity;
import domain.User;
import repo.InMemoryRepository;
import repo.Repository;
import repo.exception.UserValidator;
import repo.exception.ValidationException;
import repo.exception.Validator;

public class Main {
    public static void main(String[] args) {
        Validator<User> val= new UserValidator();
        Repository<Long, User> repo = new InMemoryRepository<Long, User>(val);
        User u1 = new User("Mimi");
        User u2 = new User("Mumu");
        User u3 = new User("Mama");

        u1.setId(1L);
        u2.setId(2L);
        u3.setId(3L);
        try {
            repo.save(u1);
            repo.save(u2);
            repo.save(u3);
        }catch(ValidationException e){
            System.out.println(e.getMessage());
        }
        //repo.findAll().forEach(x-> System.out.println(x));
        repo.findAll().forEach(System.out::println);
    }
}