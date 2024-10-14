package org.example.repository.task;

import org.example.entities.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTask(Long taskId);

    Optional<Task> findById(Long taskId);

    List<Task> findAll();

}
