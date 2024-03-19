package com.axonactive.dojo.department.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DeleteDepartmentRequestDTO {
    @NotNull(message = "Department's id is required")
    private Long id;
}
