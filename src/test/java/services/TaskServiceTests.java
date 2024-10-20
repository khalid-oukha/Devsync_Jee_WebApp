package services;

import org.example.Helpers.TaskValidator;
import org.example.entities.Tag;
import org.example.entities.Task;
import org.example.entities.User;
import org.example.entities.enums.Task_Status;
import org.example.repository.task.TaskRepository;
import org.example.services.TaskService;
import org.example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskValidator taskValidator;

    @InjectMocks
    private TaskService taskService;



    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_should_return_errors_when_assignedToId_is_null(){
        Task task = Task.builder()
                .title("Task 1")
                .status(Task_Status.TODO)
                .startDate(LocalDate.now().plusDays(4))
                .endDate(LocalDate.now().plusDays(5))
                .createdBy(User.builder().id(1L).build())
                .createdAt(LocalDateTime.now())
                .tags(new HashSet<>(List.of(Tag.builder().id(1L).name("Tag 1").build())))
                .build();

        List<String> errors = taskService.createTask(task, null);

        assertEquals(1, errors.size(), "Should have 1 error");
        assertEquals("Assigned user ID is required.", errors.get(0), "Error message should match");
    }
    @Test
    void findAll_should_return_list_of_tasks() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = Task.builder()
                .id(1L)
                .title("Task 1")
                .status(Task_Status.TODO)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .createdBy(User.builder().id(1L).build())
                .assignedTo(User.builder().id(2L).build())
                .createdAt(LocalDateTime.now())
                .tags(new HashSet<>(List.of(Tag.builder().id(1L).name("Tag 1").build())))
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .title("Task 2")
                .status(Task_Status.TODO)
                .startDate(LocalDate.now().plusDays(3))
                .endDate(LocalDate.now().plusDays(7))
                .createdBy(User.builder().id(1L).build())
                .assignedTo(User.builder().id(2L).build())
                .createdAt(LocalDateTime.now())
                .tags(new HashSet<>(List.of(Tag.builder().id(1L).name("Tag 1").build())))
                .build();

        tasks.add(task1);
        tasks.add(task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> afterTasks = taskService.findAll();

        assertNotNull(afterTasks, "Tasks list should not be null");
        assertEquals(2, afterTasks.size(), "Should return exact number of tasks");
        assertEquals("Task 1", afterTasks.get(0).getTitle(), "First task's title should match");
        assertEquals("Task 2", afterTasks.get(1).getTitle(), "Second task's title should match");

        verify(taskRepository, times(1)).findAll();
    }


    @Test
    void findAll_should_return_emptyList_when_no_tasks(){
        List<Task> emptyList = new ArrayList<>();

        assertEquals(0, emptyList.size());
        when(taskRepository.findAll()).thenReturn(emptyList);

        List<Task> result = taskService.findAll();

        assertEquals(emptyList.size(), result.size());
    }

    @Test
    void createTask_should_create_task_if_no_errors() {
        User expectedAssignTo = User.builder()
                .id(1L)
                .username("khalidVii")
                .firstName("Dadad")
                .lastName("Hasan")
                .email("oukhakhalid@gmail.com")
                .password("oukha")
                .build();

        Task task = Task.builder()
                .id(1000L)
                .title("Task 1")
                .assignedTo(expectedAssignTo)
                .status(Task_Status.TODO)
                .isLocked(false)
                .startDate(LocalDate.now().plusDays(4))
                .endDate(LocalDate.now().plusDays(8))
                .createdBy(User.builder().id(1L).build())
                .createdAt(LocalDateTime.now())
                .tags(new HashSet<>(List.of(Tag.builder().id(1L).name("Tag 1").build())))
                .build();

        when(userService.findById(2L)).thenReturn(Optional.of(User.builder().id(2L).build()));

        when(taskRepository.findById(1000L)).thenReturn(Optional.of(task));

        List<String> errors = taskService.createTask(task, "2");


        Task createdTask = taskService.findById(1000L);

        assertNotNull(createdTask, "Created task should not be null");
        assertEquals(task.getAssignedTo().getId(), createdTask.getAssignedTo().getId(), "Assigned user should match");
        assertEquals(task.getCreatedAt(), createdTask.getCreatedAt(), "Created at date should match");
        assertEquals(task.getCreatedBy(), createdTask.getCreatedBy(), "Created by user should match");
        assertEquals(task.getEndDate(), createdTask.getEndDate(), "End date should match");
        assertEquals(task.getStartDate(), createdTask.getStartDate(), "Start date should match");
        assertTrue(errors.isEmpty(), "Should not have any errors");
        verify(taskRepository, times(1)).createTask(task);
    }



    @Test
    void createTask_should_return_errors_when_dates_are_invalid() {
        Task task = Task.builder()
                .title("Task 1")
                .status(Task_Status.TODO)
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now())
                .createdBy(User.builder().id(1L).build())
                .createdAt(LocalDateTime.now())
                .tags(new HashSet<>(List.of(Tag.builder().id(1L).name("Tag 1").build())))
                .build();

        List<String> errors = taskService.createTask(task, "2");

        assertTrue(errors.contains("Invalid date range: Start date must be after today and end date after the start date."),
                "First error message should match");
        assertTrue(errors.contains("Start date must be at least 3 days in the future and end date must be after the start date."),
                "Second error message should match");
    }


    @Test
    void updateTask_should_return_errors_when_task_is_null(){
        Task task = null;
        List<String> errors = taskService.updateTask(task, User.builder().id(1L).build());

        assertEquals(1, errors.size(), "Should have 1 error");
        assertEquals("Task not found.", errors.get(0), "Error message should match");
    }

    @Test
    void updateTask_should_return_errors_when_task_is_locked(){
        Task task = Task.builder()
                .isLocked(true)
                .build();
        List<String> errors = taskService.updateTask(task, User.builder().id(1L).build());

        assertEquals(1, errors.size(), "Should have 1 error");
        assertEquals("This task is locked; it can't be deleted or updated.", errors.get(0), "Error message should match");
    }


    @Test
    void updateTask_should_update_task_when_no_errors() {
        Task oldTask = Task.builder()
                .id(1L)
                .title("Old Task Title")
                .startDate(LocalDate.now().plusDays(5))
                .endDate(LocalDate.now().plusDays(10))
                .isLocked(false)
                .build();

        when(taskRepository.findById(oldTask.getId())).thenReturn(Optional.of(oldTask));

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Updated Task Title")
                .startDate(LocalDate.now().plusDays(3))
                .endDate(LocalDate.now().plusDays(8))
                .isLocked(false)
                .build();

        List<String> errors = taskService.updateTask(updatedTask, User.builder().isManager(true).build());

        assertTrue(errors.isEmpty(), "Should not have any errors");
        verify(taskRepository, times(1)).updateTask(updatedTask);
        assertEquals("Updated Task Title", updatedTask.getTitle(), "Title should be updated");
        assertEquals(LocalDate.now().plusDays(3), updatedTask.getStartDate(), "Start date should be updated");
        assertEquals(LocalDate.now().plusDays(8), updatedTask.getEndDate(), "End date should be updated");
    }


    @Test
    void deleteTask_should_return_errors_when_task_is_null(){
        List<String> errors = taskService.deleteTask(1L, User.builder().id(1L).build());

        assertEquals(1, errors.size(), "Should have 1 error");
        assertEquals("Task not found.", errors.get(0), "Error message should match");
    }

    @Test
    void deleteTask_should_return_errors_when_task_is_locked() {
        Task task = Task.builder()
                .id(1L)
                .assignedTo(User.builder().id(2L).build())
                .createdBy(User.builder().id(1L).build())
                .isLocked(true)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        List<String> errors = taskService.deleteTask(1L, User.builder().id(1L).build());

        assertEquals(1, errors.size(), "Should have 1 error");
        assertEquals("this task is locked can't be deleted or updated", errors.get(0), "Error message should match");
    }

    @Test
    void deleteTask_should_return_errors_when_user_not_manager_and_task_not_created_by_user() {
        Task task = Task.builder()
                .id(1L)
                .assignedTo(User.builder().id(2L).build())
                .createdBy(User.builder().id(3L).build())
                .isLocked(false)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        List<String> errors = taskService.deleteTask(1L, User.builder().id(1L).isManager(false).build());

        assertEquals(1, errors.size(), "Should have 1 error");
        assertEquals("You do not have permission to delete this task.", errors.get(0), "Error message should match");
    }

    @Test
    void deleteTask_should_delete_task_when_manager_deletes() {
        Task task = Task.builder()
                .id(1L)
                .title("Task 1")
                .status(Task_Status.TODO)
                .isLocked(false)
                .createdBy(User.builder().id(3L).build())
                .assignedTo(User.builder().id(2L).build())
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertNotNull(taskService.findById(1L), "Task should exist before deletion");

        List<String> errors = taskService.deleteTask(1L, User.builder().id(3L).isManager(true).build());

        assertTrue(errors.isEmpty(), "Should not have any errors");

    }


    @Test
    void deleteTask_should_return_errors_when_user_is_not_assigned() {
        Task task = Task.builder()
                .id(1L)
                .isLocked(false)
                .assignedTo(User.builder().id(2L).build())
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        List<String> errors = taskService.deleteTask(1L, User.builder().id(1L).isManager(false).build());

        assertEquals(1, errors.size(), "Should have 1 error");
        assertEquals("You do not have permission to delete this task.", errors.get(0), "Error message should match");

        verify(taskRepository, never()).deleteTask(1L);
        assertNotNull(taskService.findById(1L), "Task should still exist after failed deletion");
    }


    @Test
    void deleteTask_should_return_errors_when_no_delete_tokens() {
        Task task = Task.builder()
                .id(1L)
                .isLocked(false)
                .assignedTo(User.builder().id(2L).build())
                .build();

        User user = User.builder()
                .id(2L)
                .deleteToken(0)
                .isManager(false)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        List<String> errors = taskService.deleteTask(1L, user);

        assertEquals(1, errors.size(), "Should have 1 error");
        assertEquals("You have already used your delete token and cannot delete this task.", errors.get(0), "Error message should match");
    }


    @Test
    void deleteTask_should_delete_own_task_when_no_errors() {
        Task task = Task.builder()
                .id(1L)
                .isLocked(false)
                .assignedTo(User.builder().id(2L).isManager(false).build())
                .createdBy(User.builder().id(2L).isManager(false).build())
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        List<String> errors = taskService.deleteTask(1L, User.builder().id(2L).deleteToken(1).isManager(false).build()); // Initialize isManager

        assertTrue(errors.isEmpty(), "Should not have any errors");
        verify(taskRepository, times(1)).deleteTask(1L);
    }


    @Test
    void deleteTask_should_update_user_tokens_after_deletion() {
        Task task = Task.builder()
                .id(1L)
                .isLocked(false)
                .assignedTo(User.builder().id(2L).build())
                .createdBy(User.builder().id(1L).build())
                .build();

        User user = User.builder()
                .id(2L)
                .deleteToken(1)
                .isManager(false)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        List<String> errors = taskService.deleteTask(1L, user);

        assertTrue(errors.isEmpty(), "Should not have any errors");
        verify(taskRepository, times(1)).deleteTask(1L);
        assertEquals(0, user.getDeleteToken(), "User's delete token should be reset to 0 after deletion"); // Assert that the token was reset
        verify(userService, times(1)).update(user); // Ensure user is updated
    }

    @Test
    void findAllTodoTasks_should_return_todo_tasks() {
        Task todoTask = Task.builder().status(Task_Status.TODO).build();
        Task inProgressTask = Task.builder().status(Task_Status.IN_PROGRESS).build();
        Task doneTask = Task.builder().status(Task_Status.DONE).build();

        when(taskRepository.findAll()).thenReturn(Arrays.asList(todoTask, inProgressTask, doneTask));

        List<Task> todoTasks = taskService.findAllTodoTasks();

        assertEquals(1, todoTasks.size(), "Should return 1 TODO task");
        assertEquals(Task_Status.TODO, todoTasks.get(0).getStatus(), "Task status should be TODO");
    }

    @Test
    void findAllInProgressTasks_should_return_in_progress_tasks() {
        Task todoTask = Task.builder().status(Task_Status.TODO).build();
        Task inProgressTask = Task.builder().status(Task_Status.IN_PROGRESS).build();
        Task doneTask = Task.builder().status(Task_Status.DONE).build();

        when(taskRepository.findAll()).thenReturn(Arrays.asList(todoTask, inProgressTask, doneTask));

        List<Task> inProgressTasks = taskService.findAllInProgressTasks();

        assertEquals(1, inProgressTasks.size(), "Should return 1 IN_PROGRESS task");
        assertEquals(Task_Status.IN_PROGRESS, inProgressTasks.get(0).getStatus(), "Task status should be IN_PROGRESS");
    }

    @Test
    void findAllDoneTasks_should_return_done_tasks() {
        Task todoTask = Task.builder().status(Task_Status.TODO).build();
        Task inProgressTask = Task.builder().status(Task_Status.IN_PROGRESS).build();
        Task doneTask = Task.builder().status(Task_Status.DONE).build();

        when(taskRepository.findAll()).thenReturn(Arrays.asList(todoTask, inProgressTask, doneTask));

        List<Task> doneTasks = taskService.findAllDoneTasks();

        assertEquals(1, doneTasks.size(), "Should return 1 DONE task");
        assertEquals(Task_Status.DONE, doneTasks.get(0).getStatus(), "Task status should be DONE");
    }

    @Test
    void requestModification_should_return_error_when_task_not_found() {
        Long taskId = 1L;
        User loggedInUser = User.builder().isManager(false).build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        List<String> errors = taskService.requestModification(taskId, loggedInUser);

        // Assert
        assertEquals(1, errors.size(), "Should return one error");
        assertEquals("Task not found.", errors.get(0), "Error message should indicate task not found");
    }

    @Test
    void requestModification_should_return_error_when_task_is_locked() {
        Long taskId = 1L;
        User loggedInUser = User.builder().isManager(true).build();

        Task lockedTask = Task.builder().id(taskId).isLocked(true).isModificationRequested(false).build();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(lockedTask));

        List<String> errors = taskService.requestModification(taskId, loggedInUser);

        assertEquals(1, errors.size(), "Should return one error");
        assertEquals("Modification request already exists or task is locked.", errors.get(0),
                "Error message should indicate task is locked");
    }

    @Test
    void requestModification_should_return_error_when_modification_requested_already() {
        Long taskId = 1L;
        User loggedInUser = User.builder().isManager(true).build();

        Task modificationRequestedTask = Task.builder().id(taskId).isLocked(false).isModificationRequested(true).build();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(modificationRequestedTask));

        List<String> errors = taskService.requestModification(taskId, loggedInUser);

        assertEquals(1, errors.size(), "Should return one error");
        assertEquals("Modification request already exists or task is locked.", errors.get(0),
                "Error message should indicate modification request already exists");
    }

    @Test
    void requestModification_should_process_modification_request_when_user_is_manager() {
        Long taskId = 1L;
        User loggedInUser = User.builder().isManager(true).build(); // Is a manager

        Task task = Task.builder().id(taskId).isLocked(false).isModificationRequested(false).assignedTo(User.builder().updateToken(1).build()).build();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        List<String> errors = taskService.requestModification(taskId, loggedInUser);

        assertEquals(0, errors.size(), "Should not return any errors");
        assertTrue(task.isModificationRequested(), "Task should have modification requested set to true");
        verify(taskRepository, times(1)).updateTask(task); // Verify updateTask was called
        assertEquals(0, task.getAssignedTo().getUpdateToken(), "Assigned user update token should be reset to 0");
        verify(userService, times(1)).update(task.getAssignedTo()); // Verify user update was called
    }

}