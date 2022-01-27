package com.iam.user.service;

import com.iam.user.dto.ApiResponse;
import com.iam.user.dto.UserDto;
import com.iam.user.entity.User;
import com.iam.user.exception.custom.UserAlreadyRegisteredException;
import com.iam.user.exception.custom.UserManagementException;
import com.iam.user.exception.custom.UserNotFoundException;
import com.iam.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static com.iam.user.constants.Constant.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private UserDto userDto;
    private Optional<User> user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto("firstname", "lastname", "a@b.com", "123");
        user = Optional.of(new User(1, "firstname", "lastname", "a@b.com", "123"));
    }

    @AfterEach
    void tearDown() {
        userDto = null;
        user = null;
    }

    @Test
    @DisplayName("save user for given user already exists ")
    void saveUserForGivenUSerAlreadyExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(user);

        UserAlreadyRegisteredException ex = assertThrows(UserAlreadyRegisteredException.class, () -> userService.saveUser(userDto));

        verify(userRepository).findByEmail(anyString());
        verify(userRepository).findByPhoneNumber(anyString());

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals("601", ex.getErrorCode());
        assertEquals("User already registered", ex.getErrorMessage());
    }

    @Test
    @DisplayName("save a new user")
    void saveUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user.get());

        ApiResponse response = userService.saveUser(userDto);

        verify(userRepository).findByEmail(anyString());
        verify(userRepository).findByPhoneNumber(anyString());
        verify(userRepository).save(any(User.class));

        assertNotNull(response);
        assertNotNull(response.getData());

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    @DisplayName("save user throws exception ")
    void saveUserThrowsSomeException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("server down"));

        UserManagementException ex = assertThrows(UserManagementException.class, () -> userService.saveUser(userDto));

        verify(userRepository).findByEmail(anyString());
        verify(userRepository).findByPhoneNumber(anyString());

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals(FAILED_TO_CREATE_RESOURCE, ex.getErrorCode());
        assertEquals("server down", ex.getErrorMessage());
    }

    @Test
    @DisplayName("get all users")
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(new ArrayList<User>(Collections.singleton(user.get())));

        ApiResponse response = userService.getAllUsers();

        verify(userRepository).findAll();

        assertNotNull(response);
        assertNotNull(response.getData());

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    @DisplayName("get user by id")
    void getUserById() {
        when(userRepository.findById(anyInt())).thenReturn(user);

        ApiResponse response = userService.getUserById(1);

        verify(userRepository).findById(anyInt());

        assertNotNull(response);
        assertNotNull(response.getData());

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());

    }

    @Test
    @DisplayName("get user by id returns empty")
    void getUserByIdWhenReturnsEmpty() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUserById(1));

        verify(userRepository).findById(anyInt());

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals(INVALID_USER, ex.getErrorCode());
        assertEquals(INVALID_USER, ex.getErrorMessage());
    }

    @Test
    @DisplayName("get user by email")
    void getUserByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        ApiResponse response = userService.getUserByEmail("a@b.com");

        verify(userRepository).findByEmail(anyString());

        assertNotNull(response);
        assertNotNull(response.getData());

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    @DisplayName("get User By Email When Return EmptyUser")
    void getUserByEmailWhenReturnEmptyUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("a@b.com"));

        verify(userRepository).findByEmail(anyString());

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals(INVALID_USER, ex.getErrorCode());
        assertEquals(INVALID_USER, ex.getErrorMessage());
    }


    @Test
    @DisplayName("delete a user")
    void deleteUser() {
        when(userRepository.findById(anyInt())).thenReturn(user);

        ApiResponse response = userService.deleteUser(1);

        verify(userRepository).findById(anyInt());

        assertNotNull(response);
        assertNotNull(response.getData());

        assertEquals(204, response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    @DisplayName("delete user returns empty user")
    void deleteUserReturnEmptyUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1));

        verify(userRepository).findById(anyInt());

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals(INVALID_USER, ex.getErrorCode());
        assertEquals(INVALID_USER, ex.getErrorMessage());
    }

    @Test
    @DisplayName("update a user")
    void updateUser1() {
        when(userRepository.save(any(User.class))).thenReturn(user.get());
        when(userRepository.findById(anyInt())).thenReturn(user);
        ApiResponse response = userService.updateUser(userDto, 1);

        assertNotNull(response);
        assertNotNull(response.getData());

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    @DisplayName("update a user for given input user is empty")
    void updateUserForGivenInputUserIsEmpty() {

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDto, 1));

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals(INVALID_USER, ex.getErrorCode());
        assertEquals(INVALID_USER, ex.getErrorMessage());
    }
}