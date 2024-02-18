package com.axonactive.dojo.employee.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DeleteEmployeeRequestDTO {
    @NotNull(message = "Employee's id is required")
    private Long id;
}
