package org.example.repository.user;

import org.example.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    void create(User user);

    void update(User user);

    void delete(Long userId);

    Optional<User> findById(Long userId); // New method
}
