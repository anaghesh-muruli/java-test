package com.iam.user.controller;

import com.iam.user.dto.ApiResponse;
import com.iam.user.dto.RegisterUserDto;
import com.iam.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/user")
    public ResponseEntity<ApiResponse> addUser(@RequestBody RegisterUserDto registerUserModel) {
        ApiResponse response = userService.saveUser(registerUserModel);
        if(response.getStatus()==200) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        else {
            log.debug("bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @PostMapping("/user/{identifier}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable String identifier, RegisterUserDto registerUserDto ) {
        ApiResponse response = userService.updateUser(registerUserDto, identifier);
        if(response.getStatus()==200) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId ) {
        ApiResponse response = userService.deleteUser(userId);
        if(response.getStatus()==200) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
