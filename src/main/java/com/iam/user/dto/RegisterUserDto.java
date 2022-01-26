package com.iam.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterUserDto {


    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

}
