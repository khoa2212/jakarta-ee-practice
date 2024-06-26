package com.axonactive.dojo.assignment.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DeleteAssignmentRequestDTO {
    @NotNull(message = "Assignment's id is required")
    private long id;
}
