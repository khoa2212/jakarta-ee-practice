package com.axonactive.dojo.auth.dto;

import com.axonactive.dojo.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Role role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avatar;
    private String accessToken;
    private String refreshToken;
}
