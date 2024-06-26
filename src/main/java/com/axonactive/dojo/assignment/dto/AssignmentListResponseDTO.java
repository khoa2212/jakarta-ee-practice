package com.axonactive.dojo.assignment.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentListResponseDTO {
    private List<AssignmentDTO> assignments;
    private long totalCount;
    private int lastPage;
}
