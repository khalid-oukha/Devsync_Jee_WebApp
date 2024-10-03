package org.example.repository.user;

import org.example.entities.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    void create(User user);
}
