package com.iam.user.service;

import com.iam.user.common.ApiResponseHandler;
import com.iam.user.dto.ApiResponse;
import com.iam.user.dto.RegisterUserDto;
import com.iam.user.entity.User;
import com.iam.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 */
@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public ApiResponse getAllUsers() {
        List<User> users =  userRepository.findAll();
        return ApiResponseHandler.generateSuccessApiResponse(users, 200);
    }

    /**
     *
     * @param id UserId
     * @return ApiResponse
     */
    @Override
    public ApiResponse getUserById(Integer id) {
        Optional<User> userDb = this.userRepository.findById(id);
        if(userDb.isPresent()) {
            User user = userDb.get();
            return ApiResponseHandler.generateSuccessApiResponse(user, 200);
        }
        else {
            return ApiResponseHandler.generateFailureApiResponse("Invalid bed Id", 400);
        }
    }

    /**
     *
     * @param id UserId
     * @return ApiResponse
     */
    @Override
    public ApiResponse getUserByEmail(String email) {
        Optional<User> userDb = this.userRepository.findByEmail(email);
        if(userDb.isPresent()) {
            User user = userDb.get();
            return ApiResponseHandler.generateSuccessApiResponse(user, 200);
        }
        else {
            return ApiResponseHandler.generateFailureApiResponse("Invalid bed Id", 400);
        }
    }

    @Override
    public ApiResponse deleteUser(Integer id) {
        Optional<User> userDb = this.userRepository.findById(id);
        if(userDb.isPresent()) {
            User user = userDb.get();
            this.userRepository.delete(userDb.get());
            return ApiResponseHandler.generateSuccessApiResponse(user, 204);
        }
        else {
            return ApiResponseHandler.generateFailureApiResponse("User Not Found", 400);
        }
    }

    @Override
    public ApiResponse saveUser(RegisterUserDto registerUserDto) {

        User user = new User();
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setEmail(registerUserDto.getEmail());
        user.setPhoneNumber(registerUserDto.getPhoneNumber());
        User savedUser = userRepository.save(user);
        return ApiResponseHandler.generateSuccessApiResponse(savedUser, 200);

    }

    public ApiResponse updateUser(RegisterUserDto registerUserDto, String identifier){
        Optional<User> userDb = Optional.empty();
        if(identifier.contains("@")) {
             userDb = this.userRepository.findByEmail(identifier);
        }
        else if(identifier.matches("[0-9]+")) {
            userDb = this.userRepository.findByPhoneNumber(identifier);
        }
        else {
            return ApiResponseHandler.generateFailureApiResponse("Incorrect email/phone number", 400);
        }

        if(userDb.isPresent()) {
            User user = userDb.get();
            user.setPhoneNumber(registerUserDto.getPhoneNumber());
            user.setEmail(registerUserDto.getEmail());
            user.setFirstName(registerUserDto.getFirstName());
            user.setLastName(registerUserDto.getLastName());
            User savedUser = userRepository.save(user);
            return ApiResponseHandler.generateSuccessApiResponse(savedUser, 200);
        }
        else {
            return ApiResponseHandler.generateFailureApiResponse("User Not Found", 404);
        }
    }
}
