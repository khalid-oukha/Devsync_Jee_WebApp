package services;

import org.example.entities.User;
import org.example.repository.user.UserRepository;
import org.example.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void beforeEach(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_should_return_list_of_users(){
        User user1 = User.builder()
                .id(1000L)
                .username("khalidVii")
                .firstName("Dadad")
                .lastName("Hasan")
                .email("oukhakhalid@gmail.com")
                .password("oukha")
                .build();

        User user2 = User.builder()
                .id(1001L)
                .username("userTwo")
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .password("password123")
                .build();

        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> afterUsers = userService.findAll();

        assertEquals(2,afterUsers.size(),"Should return exact number of users");
        assertEquals("khalidVii", afterUsers.get(0).getUsername(), "First user's username should match");
        assertEquals("userTwo", afterUsers.get(1).getUsername(), "Second user's username should match");

    }

    @Test
    void findAll_should_return_emptyList_when_no_users(){
        List<User> emptyList = new ArrayList<>();

        assertEquals(0,emptyList.size());
        when(userRepository.findAll()).thenReturn(emptyList);

        List<User> result = userService.findAll();

        assertEquals(emptyList.size(),result.size());

    }

    @Test
    void findById_should_return_user_when_user_exists(){
        User expactedUser = User.builder()
                .id(1L)
                .username("khalidVii")
                .firstName("Dadad")
                .lastName("Hasan")
                .email("oukhakhalid@gmail.com")
                .password("oukha")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(expactedUser));
        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent(),"user should be present");
        assertEquals(expactedUser,result.get(),"returned user should match the exacted user");
    }

    @Test
    void create_should_create_a_user() {
        User user = User.builder()
                .id(1000L)
                .username("alami")
                .firstName("Ahmed")
                .lastName("Hasan")
                .email("alami@gmail.com")
                .password("password123")
                .build();

        when(userRepository.findById(1000L)).thenReturn(Optional.empty());
        Optional<User> beforeUser = userService.findById(1000L);
        assertFalse(beforeUser.isPresent(), "User should not exist before creation");

        doNothing().when(userRepository).create(user);

        userService.create(user);

        when(userRepository.findById(1000L)).thenReturn(Optional.of(user));
        Optional<User> afterUser = userService.findById(1000L);

        verify(userRepository, times(1)).create(user);

        assertTrue(afterUser.isPresent(), "User should exist after creation");
        assertEquals(user.getId(), afterUser.get().getId(), "User ID should match after creation");
    }

    @Test
    void create_and_delete_should_work_properly() {
        User user = User.builder()
                .id(1000L)
                .username("alami")
                .firstName("Ahmed")
                .lastName("Hasan")
                .email("alami@gmail.com")
                .password("password123")
                .build();

        doNothing().when(userRepository).create(user);
        userService.create(user);

        when(userRepository.findById(1000L)).thenReturn(Optional.of(user));

        Optional<User> createdUser = userService.findById(1000L);
        assertTrue(createdUser.isPresent(), "User should exist after creation");
        assertEquals(user.getId(), createdUser.get().getId(), "Created user ID should match");

        doNothing().when(userRepository).delete(1000L);
        userService.delete(1000L);

        when(userRepository.findById(1000L)).thenReturn(Optional.empty());

        Optional<User> afterDelete = userService.findById(1000L);
        assertFalse(afterDelete.isPresent(), "User should no longer exist after deletion");

        verify(userRepository, times(1)).create(user);
        verify(userRepository, times(1)).delete(1000L);
    }

        @Test
        void delete_should_throw_exception_when_user_not_found() {
        when(userRepository.findById(1000L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.delete(1000L), "Should throw exception when user not found");

        verify(userRepository, never()).delete(1000L);
         }

    @Test
    void update_should_update_existing_user() {
        User existingUser = User.builder()
                .id(1000L)
                .username("alami")
                .firstName("Ahmed")
                .lastName("Hasan")
                .email("alami@gmail.com")
                .password("password123")
                .build();

        User updatedUser = User.builder()
                .id(1000L)
                .username("alamiUpdated")
                .firstName("Ahmed")
                .lastName("Hasan")
                .email("alamiUpdated@gmail.com")
                .password("newpassword")
                .build();

        when(userRepository.findById(1000L)).thenReturn(Optional.of(existingUser));

        doNothing().when(userRepository).update(updatedUser);

        userService.update(updatedUser);

        verify(userRepository, times(1)).update(updatedUser);

        when(userRepository.findById(1000L)).thenReturn(Optional.of(updatedUser));

        Optional<User> afterUpdateUser = userService.findById(1000L);
        assertTrue(afterUpdateUser.isPresent(), "User should exist after update");
        assertEquals(updatedUser.getUsername(), afterUpdateUser.get().getUsername(), "Updated username should match");
        assertEquals(updatedUser.getEmail(), afterUpdateUser.get().getEmail(), "Updated email should match");
    }

}
