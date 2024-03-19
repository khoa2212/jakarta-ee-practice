package com.axonactive.dojo.department_location.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DeleteDepartmentLocationRequestDTO {
    @NotNull(message = "Department location's id is required")
    private long id;
}
