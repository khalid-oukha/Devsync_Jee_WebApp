package org.example.repository.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityTransaction;
import org.example.entities.User;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager em;

    public UserRepositoryImpl() {
        em = Persistence.createEntityManagerFactory("myJPAUnit").createEntityManager();
    }

    @Override
    public List<User> findAll() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        List<User> users = null;
        try {
            users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void create(User user) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.merge(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long userId) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            User user = em.find(User.class, userId);
            if (user != null) {
                em.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findById(Long userId) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        User user = null;
        try {
            user = em.find(User.class, userId);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User getUser(Long id) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        User user = null;
        try {
            user = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return user;
    }
}
