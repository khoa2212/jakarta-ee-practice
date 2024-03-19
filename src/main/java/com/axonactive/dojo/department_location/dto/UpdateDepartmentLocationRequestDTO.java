package com.axonactive.dojo.department_location.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateDepartmentLocationRequestDTO {
    @NotNull(message = "Department location's id is required")
    private long id;

    @NotBlank(message = "Location must not be blank")
    private String location;

    @NotNull(message = "Department's is required")
    private long departmentId;
}
