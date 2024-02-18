package com.axonactive.dojo.assignment.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateAssignmentRequestDTO {

    @NotNull(message = "Assignment's id is required")
    private Long id;

    @NotNull(message = "Project's id is required")
    private Long projectId;

    @NotNull(message = "Employee's id is required")
    private Long employeeId;

    @NotNull(message = "Number of hour is required")
    private Integer numberOfHour;
}