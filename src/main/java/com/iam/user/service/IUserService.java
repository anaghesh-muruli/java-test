package com.iam.user.service;

import com.iam.user.dto.ApiResponse;
import com.iam.user.dto.UserDto;

/**
 *
 */
public interface IUserService {

    ApiResponse getAllUsers();
    ApiResponse saveUser(UserDto registerUserModel);
    ApiResponse getUserById(Integer id);
    ApiResponse getUserByEmail(String email);
    ApiResponse deleteUser(Integer id);

}
