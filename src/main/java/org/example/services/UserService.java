package org.example.services;

import org.example.entities.User;
import org.example.repository.user.UserRepository;
import org.example.repository.user.UserRepositoryImpl;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void create(User user) {
        userRepository.create(user);
    }
}
