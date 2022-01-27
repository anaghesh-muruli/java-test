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

    @NotEmpty
    @NotBlank
    @Pattern(regexp="^[A-Za-z]*$",message = "{validation.firstName.noSpecialCharacters}")
    @Length(min = 3, max = 50)
    private String firstName;

    @NotEmpty
    @NotBlank
    @Pattern(regexp="^[A-Za-z]*$",message = "{validation.lastName.noSpecialCharacters}")
    @Length(min = 3, max = 50)
    private String lastName;

    @NotEmpty(message = "{validation.mail.notEmpty}")
    @NotBlank(message = "{validation.mail.notEmpty}")
    @Length(message = "Email length should be between 6 and 255 characters", min = 6, max = 255)
    @Email(message="Please provide a valid email address")
    @Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
    private String email;

    @NotEmpty
    @NotBlank
    @Digits(fraction = 0, integer = 12)
    private String phoneNumber;

}
