package org.example.Helpers;

import org.example.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskValidator {

    public List<String> validateTask(Task task) {
        List<String> errors = new ArrayList<>();

        if (task.getStartDate() == null || task.getEndDate() == null) {
            errors.add("Start date and end date cannot be null.");
        } else if (!DateHelper.isValidDateRange(task.getStartDate(), task.getEndDate())) {
            errors.add("Invalid date range: Start date must be after today and end date after the start date.");
        }

        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            errors.add("Task title cannot be empty.");
        }

        return errors;
    }

}
