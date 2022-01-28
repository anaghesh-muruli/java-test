package com.iam.user.controller;

import com.iam.user.common.ApiResponseHandler;
import com.iam.user.dto.ApiResponse;
import com.iam.user.dto.UserDto;
import com.iam.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDto userDto;
    private ApiResponse apiResponse;

    @BeforeEach
    void setUp() {
        userDto = new UserDto("firstname", "lastname", "a@b.com", "123");
    }

    @AfterEach
    void tearDown() {
        userDto = null;
    }

    @Test
    @DisplayName("update a user for given input user is empty")
    void addUserForGivenStatusIsCreated() {
        apiResponse = ApiResponseHandler.generateSuccessApiResponse(userDto, HttpStatus.CREATED.value());

        when(userService.saveUser(any(UserDto.class))).thenReturn(apiResponse);

        ResponseEntity<ApiResponse> response = userController.addUser(userDto);

        verify(userService).saveUser(any(UserDto.class));

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    @DisplayName("update a user for given input user is empty")
    void getUser() {
        apiResponse = ApiResponseHandler.generateSuccessApiResponse(userDto, HttpStatus.OK.value());

        when(userService.getUserById(anyLong())).thenReturn(apiResponse);

        ResponseEntity<ApiResponse> response = userController.getUser(1);

        verify(userService).getUserById(anyLong());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    @DisplayName("update a user for given input user is empty")
    void getAllUsers() {
        apiResponse = ApiResponseHandler.generateSuccessApiResponse(userDto, HttpStatus.OK.value());

        when(userService.getAllUsers(1,1,"id")).thenReturn(apiResponse);

        ResponseEntity<ApiResponse> response = userController.getAllUsers(1,1,"id");

        verify(userService).getAllUsers(1,1,"id");

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    @DisplayName("update a user for given input user is empty")
    void updateUser() {
        apiResponse = ApiResponseHandler.generateSuccessApiResponse(userDto, HttpStatus.CREATED.value());

        when(userService.updateUser(any(UserDto.class), anyLong())).thenReturn(apiResponse);

        ResponseEntity<ApiResponse> response = userController.updateUser(1, userDto);

        verify(userService).updateUser(any(UserDto.class), anyLong());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
    }

    @Test
    @DisplayName("update a user for given input user is empty")
    void deleteUser() {
        apiResponse = ApiResponseHandler.generateSuccessApiResponse(userDto, HttpStatus.OK.value());

        when(userService.deleteUser(anyLong())).thenReturn(apiResponse);

        ResponseEntity<ApiResponse> response = userController.deleteUser(1);

        verify(userService).deleteUser(anyLong());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());

        assertEquals(204, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
    }
}