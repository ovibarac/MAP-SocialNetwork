
package repo;
import domain.Friendship;
import domain.User;
import repo.exception.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDbRepo implements Repository<Long, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private Validator<Friendship> validator;

    Repository<Long, User> userRepo;

    public FriendshipDbRepo(String url, String username, String password, Validator<Friendship> validator, Repository<Long, User> userRepo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.userRepo=userRepo;
    }
    @Override
    public Optional<Friendship> findOne(Long aLong) {
        String sql = "SELECT * from \"friendships\" where id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                //TODO add date again
                Long id = resultSet.getLong("id");
                Long idU1 = resultSet.getLong("u1");
                Long idU2 = resultSet.getLong("u2");
                Date date = resultSet.getDate("date");
                LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

                Friendship f = createFriendshipAndAddToUsers(id, idU1, idU2);

                return Optional.of(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return Optional.empty();
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"friendships\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idU1 = resultSet.getLong("u1");
                Long idU2 = resultSet.getLong("u2");
                Date date = resultSet.getDate("date");
                LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

                Friendship f = createFriendshipAndAddToUsers(id, idU1, idU2);

                friendships.add(f);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {

        String sql = "insert into \"friendships\" (id, u1, u2 ) values (DEFAULT, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
//
//             ZonedDateTime zdt = entity.getFriendsFrom().atZone(ZoneId.systemDefault());
//             Date date = (Date) Date.from(zdt.toInstant());

            //TODO save date 
            ps.setLong(1, entity.getU1().getId());
            ps.setLong(2, entity.getU2().getId());
//            ps.setDate(3, date);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Friendship> delete(Long aLong) {
        String sql = "delete from \"friendships\" where id=?";
        //TODO make abstract db repo, everything is the same except the table name
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            Optional<Friendship> f = findOne(aLong);
            ps.setLong(1,aLong);
            ps.execute();
            return f;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        String sql = "update \"friendships\" set u1=?, u2=? where id=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getU1().getId());
            ps.setLong(2, entity.getU2().getId());
            ps.setLong(3,entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
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
