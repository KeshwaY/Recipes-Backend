package me.damian.ciepiela.recipes.user;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    public User createUser(User user) {
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        if (userByEmail.isPresent() || userByUsername.isPresent()) {
            throw new IllegalStateException();
        }

        user.setTimeCreated(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail).orElseThrow(IllegalStateException::new);
    }


}
