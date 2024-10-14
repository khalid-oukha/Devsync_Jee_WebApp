package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.entities.Tag;
import org.example.entities.Task;
import org.example.entities.User;
import org.example.entities.enums.Task_Status;
import org.example.services.TaskService;
import org.example.services.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {

    private TaskService taskService;
    private UserService userService;

    @Override
    public void init() {
        taskService = new TaskService();
        userService = new UserService();
    }

    private void loadTaskBoard(HttpServletRequest request) {
        List<Task> todoTasks = taskService.findAllTodoTasks();
        request.setAttribute("todoTasks", todoTasks);

        List<Task> inProgressTasks = taskService.findAllInProgressTasks();
        request.setAttribute("inProgressTasks", inProgressTasks);

        List<Task> doneTasks = taskService.findAllDoneTasks();
        request.setAttribute("doneTasks", doneTasks);

        List<Task> requestToModifyTasks = taskService.filterByModificationRequest();
        request.setAttribute("requestToModifyTasks", requestToModifyTasks);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskId = request.getParameter("editTaskId");
        String newTask = request.getRequestURI().substring(request.getContextPath().length());

        if (newTask.equals("/tasks/new")) {
            List<User> users = userService.findAll();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/createTask.jsp").forward(request, response);
        } else if (taskId != null) {
            Task task = taskService.findById(Long.parseLong(taskId));
            if (task != null) {
                request.setAttribute("taskToEdit", task);
                request.setAttribute("Task_Status", Task_Status.values());
                request.getRequestDispatcher("/editTask.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Task not found");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else {
            loadTaskBoard(request);
            request.getRequestDispatcher("/tasks.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        switch (action) {
            case "requestModification": {
                Long taskId = Long.parseLong(request.getParameter("taskId"));
                List<String> errors = taskService.requestModification(taskId, loggedInUser);

                if (!errors.isEmpty()) {
                    request.setAttribute("errors", errors);
                    loadTaskBoard(request);
                    request.getRequestDispatcher("/tasks.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/tasks");
                }
                break;
            }
            case "delete": {
                List<String> errors = taskService.deleteTask(Long.parseLong(request.getParameter("taskId")), loggedInUser);
                if (!errors.isEmpty()) {
                    request.setAttribute("errors", errors);
                    loadTaskBoard(request);
                    request.getRequestDispatcher("/tasks.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/tasks");
                }
                break;
            }
            case "edit": {
                Long taskId = Long.parseLong(request.getParameter("taskId"));
                Task task = taskService.findById(taskId);

                if (task != null) {
                    task.setTitle(request.getParameter("title"));
                    task.setStartDate(LocalDate.parse(request.getParameter("startDate")));
                    task.setEndDate(LocalDate.parse(request.getParameter("end_date")));
                    List<User> users = userService.findAll();
                    request.setAttribute("users", users);
                    
                    Set<Tag> tags = taskService.parseTags(request.getParameter("tags"));
                    task.setTags(tags);
                    String taskStatusParam = request.getParameter("taskStatus");
                    Task_Status newStatus = Task_Status.valueOf(taskStatusParam);
                    task.setStatus(newStatus);

                    List<String> errors = taskService.updateTask(task, loggedInUser);

                    if (!errors.isEmpty()) {
                        request.setAttribute("errors", errors);
                        request.setAttribute("taskToEdit", task);

                        request.getRequestDispatcher("/editTask.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/tasks");
                    }
                } else {
                    request.setAttribute("error", "Task not found");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }
                break;
            }
            default: {
                String title = request.getParameter("title");
                String assignedToId = request.getParameter("assignedTo");
                String startDate = request.getParameter("startDate");
                String endDate = request.getParameter("end_date");
                String tagsInput = request.getParameter("tags");

                Task task = new Task();
                task.setTitle(title);
                task.setStartDate(LocalDate.parse(startDate));
                task.setEndDate(LocalDate.parse(endDate));

                Set<Tag> tags = taskService.parseTags(tagsInput);
                task.setTags(tags);
                task.setCreatedBy(loggedInUser);

                List<String> errors = taskService.createTask(task, assignedToId);

                if (!errors.isEmpty()) {
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher("/createTask.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/tasks");
                }
                break;
            }
        }
    }
}
