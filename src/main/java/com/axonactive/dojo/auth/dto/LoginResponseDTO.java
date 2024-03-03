package com.axonactive.dojo.auth.dto;

import com.axonactive.dojo.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginResponseDTO {

    private String displayName;
    private String email;
    private String accessToken;
    private String refreshToken;
    private Role role;
}
