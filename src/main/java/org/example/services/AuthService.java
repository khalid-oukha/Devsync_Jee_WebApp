package org.example.services;

import jakarta.servlet.http.HttpSession;
import org.example.entities.User;
import org.example.repository.auth.AuthRepository;
import org.example.repository.auth.AuthRepositoryImpl;

import java.util.Optional;

public class AuthService {

    private final AuthRepository authRepository;

    public AuthService() {
        this.authRepository = new AuthRepositoryImpl();
    }

    public User login(String email, String password, HttpSession session) {
        Optional<User> user = authRepository.login(email, password);

        if (user.isPresent()) {
            session.setAttribute("loggedInUser", user.get());
            session.setAttribute("username", user.get().getUsername());
            session.setAttribute("isManager", user.get().getIsManager());
            return user.get();
        } else {
            return null;
        }
    }

    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}
