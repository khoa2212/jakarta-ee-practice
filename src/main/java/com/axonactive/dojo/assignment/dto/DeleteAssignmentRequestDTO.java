package com.axonactive.dojo.assignment.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteAssignmentRequestDTO {
    @NotNull(message = "Assignment's id is required")
    private Long id;
}
