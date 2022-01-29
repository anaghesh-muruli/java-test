package com.iam.user.service;

import com.iam.user.dto.ApiResponse;
import com.iam.user.dto.UserDto;

/**
 *
 */
public interface IUserService {

    ApiResponse saveUser(UserDto registerUserModel);

    ApiResponse getUserById(long id);

    ApiResponse getUserByEmail(String email);

    ApiResponse deleteUser(long id);

    ApiResponse updateUser(UserDto registerUserDto, long userId);

    ApiResponse getAllUsers(Integer offset, Integer pageSize, String field);
}
