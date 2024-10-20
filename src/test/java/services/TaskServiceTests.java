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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        // Arrange
        Task task = Task.builder()
                .title("Task 1")
                .assignedTo(User.builder().id(2L).build())
                .status(Task_Status.TODO)
                .isLocked(false)
                .startDate(LocalDate.now().plusDays(4))
                .endDate(LocalDate.now().plusDays(8))
                .createdBy(User.builder().id(1L).build())
                .createdAt(LocalDateTime.now())
                .tags(new HashSet<>(List.of(Tag.builder().id(1L).name("Tag 1").build())))
                .build();

        when(taskValidator.validateTask(task)).thenReturn(List.of());

        when(userService.findById(2L)).thenReturn(Optional.of(User.builder().id(2L).build()));

        List<String> errors = taskService.createTask(task, "2");

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
        // Arrange
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



}
