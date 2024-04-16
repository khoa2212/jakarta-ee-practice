package com.axonactive.dojo.relative.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RelativeMessageDTO {
    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String gender;

    @Size(min = 9, max = 15, message = "Phone number must be in range 9 - 15 numbers")
    private String phoneNumber;

    private String relationship;

    @NotNull(message = "Employee must be specify")
    private Long employeeId;
}
