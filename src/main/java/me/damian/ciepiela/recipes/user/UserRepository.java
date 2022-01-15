package me.damian.ciepiela.recipes.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    // db.users.find({email: "test2@gmail.com"})
    @Query("{email: ?0}")
    Optional<User> findByEmail(String userEmail);

    // db.users.find({username: "test"})
    @Query("{username: ?0}")
    Optional<User> findByUsername(String username);

}
