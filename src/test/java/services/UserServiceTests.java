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
        assertEquals(expactedUser,result.get(),"returned user should match the expacted user");

    }
}
