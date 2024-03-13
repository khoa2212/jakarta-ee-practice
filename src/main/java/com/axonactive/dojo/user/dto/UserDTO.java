package com.axonactive.dojo.user.dto;

import com.axonactive.dojo.enums.Role;
import com.axonactive.dojo.enums.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String displayName;
    private String email;
    private Role role;
    private Status status;
    private String avatar;
}
