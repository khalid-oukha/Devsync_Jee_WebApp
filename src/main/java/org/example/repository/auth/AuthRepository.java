package org.example.repository.auth;

import org.example.entities.User;

import java.util.Optional;

public interface AuthRepository {
    Optional<User> login(String email, String password);
}
