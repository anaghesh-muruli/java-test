package com.iam.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    @NotBlank(message = "{validation.firstName.notEmpty}")
    @Pattern(regexp="^[A-Za-z]*$",message = "{validation.firstName.noSpecialCharacters}")
    @Length(min = 3, max = 80, message = "{validation.firstName.length}")
    private String firstName;

    @NotBlank(message = "{validation.lastName.notEmpty}")
    @Pattern(regexp="^[A-Za-z]*$",message = "{validation.lastName.noSpecialCharacters}")
    @Length(min = 3, max = 80, message = "{validation.lastName.length}")
    private String lastName;

    @NotBlank(message = "{validation.mail.notEmpty}")
    @Length(message = "{validation.mail.length}", min = 6, max = 255)
    @Email(message="{validation.mail.valid}")
    @Pattern(regexp=".+@.+\\..+", message="{validation.mail.valid}")
    private String email;

    @NotBlank(message = "{validation.phoneNumber.notEmpty}")
    @Digits(fraction = 0, integer = 12, message = "{validation.phoneNumber.digitsOnly}")
    @Length(min = 7, max = 14, message = "{validation.phoneNumber.length}")
    private String phoneNumber;

}
