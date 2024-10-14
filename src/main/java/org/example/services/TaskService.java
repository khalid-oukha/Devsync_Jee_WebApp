package org.example.services;

import org.example.Helpers.DateHelper;
import org.example.Helpers.TaskValidator;
import org.example.entities.Tag;
import org.example.entities.Task;
import org.example.entities.User;
import org.example.entities.enums.Task_Status;
import org.example.repository.task.TaskRepository;
import org.example.repository.task.TaskRepositoryImpl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskValidator taskValidator;
    private final UserService userService;

    public TaskService() {
        this.taskRepository = new TaskRepositoryImpl();
        this.taskValidator = new TaskValidator();
        this.userService = new UserService();
    }

    public List<String> createTask(Task task, String assignedToId) {
        List<String> errors = taskValidator.validateTask(task);

        if (assignedToId != null && !assignedToId.isEmpty()) {
            Optional<User> optionalAssignedTo = userService.findById(Long.parseLong(assignedToId));
            if (optionalAssignedTo.isPresent()) {
                User assignedTo = optionalAssignedTo.get();
                task.setAssignedTo(assignedTo);
            } else {
                errors.add("Assigned user not found.");
            }
        } else {
            errors.add("Assigned user ID is required.");
        }

        if (!DateHelper.isValidDateRange(task.getStartDate(), task.getEndDate())) {
            errors.add("Start date must be at least 3 days in the future and end date must be after the start date.");
        }


        if (errors.isEmpty()) {
            taskRepository.createTask(task);
        }

        return errors;
    }

    public List<String> updateTask(Task task, User loggedInUser) {
        List<String> errors = taskValidator.validateTask(task);
        if (task == null) {
            errors.add("Task not found.");
            return errors;
        }

        if (task.isLocked()) {
            errors.add("this task is locked can't be deleted or updated");
            return errors;
        }

        if (loggedInUser.getIsManager()) {
            taskRepository.updateTask(task);
            return errors;
        }

        if (errors.isEmpty()) {
            taskRepository.updateTask(task);
        }
        return errors;
    }

    public Task findById(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public List<String> deleteTask(Long taskId, User loggedInUser) {
        List<String> errors = new ArrayList<>();

        Task task = findById(taskId);
        if (task == null) {
            errors.add("Task not found.");
            return errors;
        }

        if (task.isLocked()) {
            errors.add("this task is locked can't be deleted or updated");
            return errors;
        }

        if (loggedInUser.getIsManager()) {
            taskRepository.deleteTask(taskId);
            return errors;
        }

        if (!task.getAssignedTo().getId().equals(loggedInUser.getId())) {
            errors.add("You do not have permission to delete this task.");
            return errors;
        }

        if (loggedInUser.getDeleteToken() == 0) {
            errors.add("You have already used your delete token and cannot delete this task.");
            return errors;
        }

        if (loggedInUser.getId().equals(task.getCreatedBy().getId())) {
            taskRepository.deleteTask(taskId);
            return errors;
        }

        taskRepository.deleteTask(taskId);

        loggedInUser.setDeleteToken(0);
        userService.update(loggedInUser);

        return errors;
    }


    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findAllTodoTasks() {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getStatus().equals(Task_Status.TODO))
                .collect(Collectors.toList());
    }

    public List<Task> findAllInProgressTasks() {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getStatus().equals(Task_Status.IN_PROGRESS))
                .collect(Collectors.toList());
    }

    public List<Task> findAllDoneTasks() {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getStatus().equals(Task_Status.DONE))
                .collect(Collectors.toList());
    }


    public List<Task> filterTasksByDate(LocalDate from_date, LocalDate to_date) {
        return taskRepository.findAll().stream().filter(task -> task.getStartDate().isAfter(from_date) && task.getEndDate().isBefore(to_date)).collect(Collectors.toList());
    }

    public List<Task> filterByModificationRequest() {
        return taskRepository.findAll().stream().filter(Task::isModificationRequested).collect(Collectors.toList());
    }

    public Set<Tag> parseTags(String tagsInput) {
        return Arrays.stream(tagsInput.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    public List<String> requestModification(Long taskId, User loggedInUser) {
        List<String> errors = new ArrayList<>();

        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            errors.add("Task not found.");
            return errors;
        }

        if (task.isLocked() || task.isModificationRequested()) {
            errors.add("Modification request already exists or task is locked.");
            return errors;
        }

        if (loggedInUser.getIsManager()) {
            task.setModificationRequested(true);
            taskRepository.updateTask(task);

            User assignedTo = task.getAssignedTo();
            assignedTo.setUpdateToken(0);
            userService.update(assignedTo);
        }

        return errors;
    }


}
