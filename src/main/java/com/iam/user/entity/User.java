package com.iam.user.entity;

import lombok.*;
import javax.persistence.*;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.iam.user.constants.Constant.ID_MAX;
import static com.iam.user.constants.Constant.ID_MIN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_store")
public class User extends UserAudit {

    @Id
    @Column(name = "uid", nullable = false)
    private long id = ThreadLocalRandom.current().nextLong(ID_MIN,ID_MAX);

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

}
