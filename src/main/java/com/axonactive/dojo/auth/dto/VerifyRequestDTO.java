package com.axonactive.dojo.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VerifyRequestDTO {
    @NotBlank(message = "Verified token is required")
    private String verifiedToken;
}
