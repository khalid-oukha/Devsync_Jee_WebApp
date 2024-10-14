package org.example.repository.auth;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entities.User;

import java.util.Optional;

public class AuthRepositoryImpl implements AuthRepository {
    private final EntityManager em;

    public AuthRepositoryImpl() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPAUnit");
        em = emf.createEntityManager();
    }

    public Optional<User> login(String email, String password) {
        try {
            User user = em.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
            return Optional.ofNullable(user);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
