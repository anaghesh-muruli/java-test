package com.iam.user.service;

import com.iam.user.common.ApiResponseHandler;
import com.iam.user.dto.ApiResponse;
import com.iam.user.dto.UserDto;
import com.iam.user.entity.User;
import com.iam.user.exception.custom.UserAlreadyRegisteredException;
import com.iam.user.exception.custom.UserManagementException;
import com.iam.user.exception.custom.UserNotFoundException;
import com.iam.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.iam.user.constants.Constant.*;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public ApiResponse saveUser(UserDto registerUserDto) throws UserAlreadyRegisteredException {

        if(isUserAlreadyPresent(registerUserDto.getEmail(), registerUserDto.getPhoneNumber())) {
            LOGGER.debug("User already registered.");
            throw new UserAlreadyRegisteredException("601", "User already registered");
        }
        User user = new User();
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setEmail(registerUserDto.getEmail());
        user.setPhoneNumber(registerUserDto.getPhoneNumber());

        try {
            user  = userRepository.save(user);
        }
        catch (Exception exception) {
            LOGGER.debug("Saving to DB failed, " + exception.getMessage());
            throw new UserManagementException(FAILED_TO_CREATE_RESOURCE, exception.getMessage());
        }
        return ApiResponseHandler.generateSuccessApiResponse(user, HttpStatus.OK.value());

    }

    private boolean isUserAlreadyPresent(String email, String phoneNumber) {
        Optional<User> userDbEmail = this.userRepository.findByEmail(email);
        Optional<User> userDbPhone = this.userRepository.findByPhoneNumber(phoneNumber);
        return userDbPhone.isPresent() || userDbEmail.isPresent();
    }

    @Override
    public ApiResponse getAllUsers() {
        List<User> users =  userRepository.findAll();
        return ApiResponseHandler.generateSuccessApiResponse(users, HttpStatus.OK.value());
    }

    @Override
    public ApiResponse getUserById(Integer id) {
        Optional<User> userDb = this.userRepository.findById(id);
        if(userDb.isPresent()) {
            User user = userDb.get();

            return ApiResponseHandler.generateSuccessApiResponse(user, HttpStatus.OK.value());
        }
        else {
            LOGGER.info("User not found for the given userid");
            throw new UserNotFoundException(INVALID_USER, INVALID_USER);
        }
    }

    @Override
    public ApiResponse getUserByEmail(String email) {
        Optional<User> userDb = this.userRepository.findByEmail(email);
        if(userDb.isPresent()) {
            User user = userDb.get();
            return ApiResponseHandler.generateSuccessApiResponse(user, HttpStatus.OK.value());
        }
        else {
            LOGGER.info("User not found for the given userid");
            throw new UserNotFoundException(INVALID_USER, INVALID_USER);
        }
    }

    @Override
    public ApiResponse deleteUser(Integer id) {
        Optional<User> userDb = this.userRepository.findById(id);
        if(userDb.isPresent()) {
            User user = userDb.get();
            this.userRepository.delete(userDb.get());
            return ApiResponseHandler.generateSuccessApiResponse(user, HttpStatus.NO_CONTENT.value());
        }
        else {
            LOGGER.info("User not found for the given userid");
            throw new UserNotFoundException(INVALID_USER, INVALID_USER);
        }
    }


    public ApiResponse updateUser(UserDto registerUserDto, int userId){
//        Optional<User> userDb = Optional.empty();
//        if(identifier.matches(".+@.+\\..+")) {
//             userDb = this.userRepository.findByEmail(identifier);
//        }
//        else if(identifier.matches("[0-9]+")) {
//            userDb = this.userRepository.findByPhoneNumber(identifier);
//        }
//        else {
//            throw new UserManagementException(INVALID_IDENTIFIER, HttpStatus.BAD_REQUEST.toString());
//        }
        Optional<User> userDb = this.userRepository.findById(userId);
        if(userDb.isPresent()) {
            User user = userDb.get();
            user.setPhoneNumber(registerUserDto.getPhoneNumber());
            user.setEmail(registerUserDto.getEmail());
            user.setFirstName(registerUserDto.getFirstName());
            user.setLastName(registerUserDto.getLastName());
            user = userRepository.save(user);
            return ApiResponseHandler.generateSuccessApiResponse(user, 200);
        }
        else {
            LOGGER.info("User not found for the given userid");
            throw new UserNotFoundException(INVALID_USER, INVALID_USER);
        }
    }
}
