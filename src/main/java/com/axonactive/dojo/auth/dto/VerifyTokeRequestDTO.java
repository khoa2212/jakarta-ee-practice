package com.axonactive.dojo.auth.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VerifyTokeRequestDTO {

    @NotBlank(message = "Verified token is required")
    private String verifiedToken;
}
