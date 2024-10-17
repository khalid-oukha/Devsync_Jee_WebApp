package org.example.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.entities.User;
import org.example.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;
@ApplicationScoped
public class UserService {

    @Inject
    private UserRepository userRepository;

//    public UserService() {
//        userRepository = new UserRepositoryImpl();
//    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void create(User user) {
        userRepository.create(user);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(id);
        } else {
            throw new RuntimeException("User not found for ID: " + id);
        }
    }
}
