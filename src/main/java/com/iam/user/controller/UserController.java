package com.iam.user.controller;

import com.iam.user.dto.ApiResponse;
import com.iam.user.dto.UserDto;
import com.iam.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/user")
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody UserDto registerUserModel) {
        LOGGER.trace("User post Mapping invoked");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(registerUserModel));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Integer userId) {
        LOGGER.trace("User get Mapping invoked");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        LOGGER.trace("User Get(All) Mapping invoked");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Integer userId, @Valid UserDto registerUserDto ) {
        LOGGER.trace("User Put Mapping invoked");
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(registerUserDto, userId));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId ) {
        LOGGER.trace("User delete Mapping invoked");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUser(userId));
    }
}
