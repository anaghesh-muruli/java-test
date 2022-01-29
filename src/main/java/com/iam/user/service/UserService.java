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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.iam.user.constants.Constant.*;
import static com.iam.user.constants.ErrorCode.*;

@Service
public class UserService implements IUserService {

    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserRepository userRepository;


    /**
     * This method saves the user into the repository
     *
     * @param  registerUserDto
     * @return ApiResponse
     * @throws UserAlreadyRegisteredException if user already present
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResponse saveUser(UserDto registerUserDto) throws UserAlreadyRegisteredException {

        Map<String, Boolean> userRegistrationStatus = isUserAlreadyPresent(registerUserDto.getEmail(), registerUserDto.getPhoneNumber());

        LOGGER.debug("user registration status: "+ userRegistrationStatus);
        if (Boolean.TRUE.equals(userRegistrationStatus.get(EMAIL_FIELD))) {
            LOGGER.debug(String.format(EMAIL_ALREADY_PRESENT,registerUserDto.getEmail()));
            throw new UserAlreadyRegisteredException(ERROR_EMAIL_ALREADY_PRESENT, String.format(EMAIL_ALREADY_PRESENT,registerUserDto.getEmail()));
        }

        if (Boolean.TRUE.equals(userRegistrationStatus.get(PHONE_NUMBER_FIELD))) {
            LOGGER.debug(String.format(PHONE_NUMBER_ALREADY_PRESENT,registerUserDto.getPhoneNumber()));
            throw new UserAlreadyRegisteredException(ERROR_PHONE_ALREADY_PRESENT,  String.format(PHONE_NUMBER_ALREADY_PRESENT,registerUserDto.getPhoneNumber()));
        }

        User user = new User();
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setEmail(registerUserDto.getEmail());
        user.setPhoneNumber(registerUserDto.getPhoneNumber());

        try {
            LOGGER.info("Saving user to repository");
            user = userRepository.save(user);
        } catch (Exception exception) {
            //Logging and rethrowing it because it's a single threaded application
            LOGGER.debug(String.format(USER_FAILED_TO_SAVE, exception.getMessage()));
            throw new UserManagementException(FAILED_TO_CREATE_RESOURCE, exception.getMessage());
        }
        return ApiResponseHandler.generateSuccessApiResponse(user, HttpStatus.OK.value());

    }



    /**
     * This method retrieves a particular user by id
     * @return ApiResponse
     * @param  id
     */
    @Override
    public ApiResponse getUserById(long id) {
        Optional<User> userDb = this.userRepository.findById(id);
        if (userDb.isPresent()) {
            LOGGER.info(String.format(GET_USER_FROM_REPOSITORY));
            User user = userDb.get();
            return ApiResponseHandler.generateSuccessApiResponse(user, HttpStatus.OK.value());
        } else {
            LOGGER.info(USER_NOT_FOUND);
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND, USER_NOT_FOUND);
        }
    }

    /**
     * This method retrieves a particular by email
     * @return ApiResponse
     * @param  email
     */
    @Override
    public ApiResponse getUserByEmail(String email) {
        Optional<User> userDb = this.userRepository.findByEmail(email);
        if (userDb.isPresent()) {
            LOGGER.info(String.format(GET_USER_FROM_REPOSITORY));
            User user = userDb.get();
            return ApiResponseHandler.generateSuccessApiResponse(user, HttpStatus.OK.value());
        } else {
            LOGGER.info(USER_NOT_FOUND);
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND, USER_NOT_FOUND);
        }
    }

    /**
     * This method deletes the user by id
     * @return ApiResponse
     * @param  id
     */
    @Override
    public ApiResponse deleteUser(long id) {
        Optional<User> userDb = this.userRepository.findById(id);
        if (userDb.isPresent()) {
            LOGGER.info(String.format(GET_USER_FROM_REPOSITORY));
            User user = userDb.get();
            this.userRepository.delete(userDb.get());
            return ApiResponseHandler.generateSuccessApiResponse(user, HttpStatus.NO_CONTENT.value());
        } else {
            LOGGER.info(USER_NOT_FOUND);
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND, USER_NOT_FOUND);
        }
    }

    /**
     * This method updates the user
     * @return ApiResponse
     * @param  registerUserDto
     * @param  userId
     */
    @Override
    public ApiResponse updateUser(UserDto registerUserDto, long userId) {

        Optional<User> userDb = this.userRepository.findById(userId);
        if (userDb.isPresent()) {
            LOGGER.info(String.format(GET_USER_FROM_REPOSITORY));
            User user = userDb.get();
            user.setPhoneNumber(registerUserDto.getPhoneNumber());
            user.setEmail(registerUserDto.getEmail());
            user.setFirstName(registerUserDto.getFirstName());
            user.setLastName(registerUserDto.getLastName());
            try {
                LOGGER.info("Saving user to repository");
                user = userRepository.save(user);
            } catch (Exception exception) {
                //Logging and rethrowing it because it's a single threaded application
                LOGGER.debug(String.format(USER_FAILED_TO_SAVE, exception.getMessage()));
                throw new UserManagementException(FAILED_TO_CREATE_RESOURCE, exception.getMessage());
            }
            return ApiResponseHandler.generateSuccessApiResponse(user, HttpStatus.OK.value());
        } else {
            LOGGER.info(USER_NOT_FOUND);
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND, USER_NOT_FOUND);
        }
    }

    /**
     * This method checks if the user is already present
     * @return Map<String, Boolean>
     * @param  email
     * @param  phoneNumber
     */
    private Map<String, Boolean> isUserAlreadyPresent(String email, String phoneNumber) {
        Optional<User> userDbEmail = this.userRepository.findByEmail(email);
        Optional<User> userDbPhone = this.userRepository.findByPhoneNumber(phoneNumber);
        Map<String, Boolean> userStatus = new HashMap<>();
        userStatus.put(EMAIL_FIELD, userDbEmail.isPresent());
        userStatus.put(PHONE_NUMBER_FIELD, userDbPhone.isPresent());
        return userStatus;
    }

    /**
     * This method retrieves all the user from the repository
     * @return ApiResponse
     */
    @Override
    public ApiResponse getAllUsers(Integer offset, Integer pageSize, String field){
        if(field == null || field.trim().isEmpty()) {
            field = "id";
        }
        LOGGER.info("Retrieving all users from repository");
        Page<User> users = userRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));

        Map<String, Object> userPaged = new HashMap<>();
        userPaged.put("Total elements",users.getTotalElements());
        userPaged.put("Total Pages",users.getTotalPages());
        userPaged.put("Content",users.getContent());

        return ApiResponseHandler.generateSuccessApiResponse(userPaged,HttpStatus.OK.value());
    }
}
