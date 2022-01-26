package com.iam.user.service;

import com.iam.user.dto.ApiResponse;
import com.iam.user.dto.RegisterUserDto;

public interface IUserService {

    ApiResponse getAllUsers();
    ApiResponse saveUser(RegisterUserDto registerUserModel);
    ApiResponse getUserById(Integer id);

    ApiResponse getUserByEmail(String email);

    ApiResponse deleteUser(Integer id);

}
