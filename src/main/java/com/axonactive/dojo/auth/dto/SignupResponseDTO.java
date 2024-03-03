package com.axonactive.dojo.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SignupResponseDTO {
    private String verifiedToken;
}
