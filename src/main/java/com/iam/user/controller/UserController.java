package com.iam.user.controller;

import com.iam.user.dto.ApiResponse;
import com.iam.user.dto.UserDto;
import com.iam.user.service.IUserService;
import com.iam.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;

/**
 * REST endpoint for the user functionality
 *
 * @author Anaghesh Muruli
 *
 */
@RestController
@RequestMapping(value = "api/v1/users")
@Validated
public class UserController {

    @Autowired
    private IUserService userService;

    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody UserDto registerUserDto){
        LOGGER.trace("User post Mapping invoked");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(registerUserDto));
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getUser(@PathVariable("userId") @Digits(integer = 10, fraction = 0) long userId){
        LOGGER.trace("User get Mapping invoked");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllUsers(@RequestParam(name="offset", required=false)
                                                                                     Integer offset, @RequestParam(name="pageSize", required=false) Integer pageSize,
                                                                         @RequestParam(name="field", required=false) String field){
        LOGGER.trace("User Get(All) Mapping invoked");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers(offset, pageSize, field));
    }

    @PutMapping(value ="/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("userId") @Digits(integer = 10, fraction = 0) long userId , @Valid @RequestBody UserDto registerUserDto ){
        LOGGER.trace("User Put Mapping invoked");
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(registerUserDto, userId));
    }

    @DeleteMapping(value ="/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") @Digits(integer = 10, fraction = 0) long userId ) {
        LOGGER.trace("User delete Mapping invoked");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUser(userId));
    }
}
