package com.axonactive.dojo.user.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.enums.Role;
import com.axonactive.dojo.enums.Status;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String displayName;
    private String avatar;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String refreshToken;
}
