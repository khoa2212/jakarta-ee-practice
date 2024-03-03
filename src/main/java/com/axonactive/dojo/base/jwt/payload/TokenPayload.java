package com.axonactive.dojo.base.jwt.payload;

import com.axonactive.dojo.enums.Role;
import com.axonactive.dojo.enums.TokenType;
import lombok.*;

import java.security.Principal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenPayload implements Principal {
    private TokenType tokenType;

    private String email;

    private Role role;

    private String displayName;
    @Override
    public String getName() {
        return email;
    }
}
