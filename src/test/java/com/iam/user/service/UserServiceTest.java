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
import static com.iam.user.constants.ErrorCode.ERROR_USER_NOT_FOUND;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

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
        user = Optional.empty();
    }

    @Test
    @DisplayName("Save user - should throw UserAlreadyRegisteredException if user is already present")
    void saveUserThrowsExceptionForGivenUSerAlreadyExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(user);

        UserAlreadyRegisteredException ex = assertThrows(UserAlreadyRegisteredException.class, () -> userService.saveUser(userDto));

        verify(userRepository).findByEmail(anyString());
        verify(userRepository).findByPhoneNumber(anyString());

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals("601", ex.getErrorCode());
        assertEquals("User already registered with the email id: a@b.com", ex.getErrorMessage());
    }

    @Test
    @DisplayName("Save user - should save when valid data is given ")
    void saveUserShouldCreateEntry() {
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
    @DisplayName("Save user throws exception ")
    void saveUserThrowsUserManagementException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Save operation failed"));

        UserManagementException ex = assertThrows(UserManagementException.class, () -> userService.saveUser(userDto));

        verify(userRepository).findByEmail(anyString());
        verify(userRepository).findByPhoneNumber(anyString());

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals(FAILED_TO_CREATE_RESOURCE, ex.getErrorCode());
        assertEquals("Save operation failed", ex.getErrorMessage());
    }

    @Test
    @DisplayName("Get all - should return list of users")
    void getAllUsersReturnsListOfUsers() {
        when(userRepository.findAll(PageRequest.of(1, 1).withSort(Sort.by("id")))).thenReturn((new PageImpl<User>(new ArrayList<User>())));
        ApiResponse response = userService.getAllUsers(1,1,"id");
        verify(userRepository).findAll(PageRequest.of(1, 1).withSort(Sort.by("id")));

        assertNotNull(response);
        assertNotNull(response.getData());

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    @DisplayName("Get user should return a user")
    void getUserByIdShouldReturnUser() {
        when(userRepository.findById(anyLong())).thenReturn(user);

        ApiResponse response = userService.getUserById(1);

        verify(userRepository).findById(anyLong());

        assertNotNull(response);
        assertNotNull(response.getData());

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());

    }

    @Test
    @DisplayName("Get user by id returns empty when user not present")
    void getUserByIdWhenReturnsEmpty() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUserById(1));

        verify(userRepository).findById(anyLong());

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals(ERROR_USER_NOT_FOUND, ex.getErrorCode());
        assertEquals(USER_NOT_FOUND, ex.getErrorMessage());
    }

    @Test
    @DisplayName("get user by email should return user")
    void getUserByEmailShouldReturnUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        ApiResponse response = userService.getUserByEmail("a@b.com");

        verify(userRepository).findByEmail(anyString());

        assertNotNull(response);
        assertNotNull(response.getData());

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    @DisplayName("get User By Email should Return EmptyUser")
    void getUserByEmailWhenReturnEmptyUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("a@b.com"));

        verify(userRepository).findByEmail(anyString());

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals(ERROR_USER_NOT_FOUND, ex.getErrorCode());
        assertEquals(USER_NOT_FOUND, ex.getErrorMessage());
    }


    @Test
    @DisplayName("delete a user when user is present")
    void deleteUser() {
        when(userRepository.findById(anyLong())).thenReturn(user);

        ApiResponse response = userService.deleteUser(1);

        verify(userRepository).findById(anyLong());

        assertNotNull(response);
        assertNotNull(response.getData());

        assertEquals(204, response.getStatus());
        assertEquals("Success", response.getMessage());
    }

    @Test
    @DisplayName("delete user returns empty user")
    void deleteUserReturnEmptyUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1));

        verify(userRepository).findById(anyLong());

        assertNotNull(ex);
        assertNotNull(ex.getErrorMessage());
        assertNotNull(ex.getErrorCode());

        assertEquals(ERROR_USER_NOT_FOUND, ex.getErrorCode());
        assertEquals(USER_NOT_FOUND, ex.getErrorMessage());
    }

    @Test
    @DisplayName("update a user")
    void updateUserShouldUpdateWhenValidInput() {
        when(userRepository.save(any(User.class))).thenReturn(user.get());
        when(userRepository.findById(anyLong())).thenReturn(user);
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

        assertEquals(ERROR_USER_NOT_FOUND, ex.getErrorCode());
        assertEquals(USER_NOT_FOUND, ex.getErrorMessage());
    }
}