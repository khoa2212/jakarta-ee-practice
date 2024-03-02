package com.axonactive.dojo.assignment.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateAssignmentRequestDTO {

    @NotNull(message = "Project's id is required")
    private long projectId;

    @NotNull(message = "Employee's id is required")
    private long employeeId;

    @NotNull(message = "Number of hour is required")
    private int numberOfHour;
}
