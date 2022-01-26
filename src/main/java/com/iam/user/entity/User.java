package com.iam.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_store")
public class User extends UserAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false)
    private Integer userId;


    @Length(min = 3, max = 50)
    @Column(name = "first_name")
    private String firstName;

    @Length(min = 3, max = 50)
    @Column(name = "last_name")
    private String lastName;

    @Length(message = "Email length exceeded", min = 6, max = 255)
    @Email(message="Please provide a valid email address")
    @Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

}
