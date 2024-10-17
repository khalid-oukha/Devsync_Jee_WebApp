package org.example.repository.task;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entities.Task;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskRepositoryImpl implements TaskRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createTask(Task task) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(task);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void updateTask(Task task) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(task);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void deleteTask(Long taskId) {
        try {
            entityManager.getTransaction().begin();
            Task task = entityManager.find(Task.class, taskId);
            if (task != null) {
                entityManager.remove(task);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Optional<Task> findById(Long taskId) {
        Task task = entityManager.find(Task.class, taskId);
        return Optional.ofNullable(task);
    }

    @Override
    public List<Task> findAll() {
        return entityManager.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

}
