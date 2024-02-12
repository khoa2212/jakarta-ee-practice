package com.axonactive.dojo.project.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectListResponseDTO {
    private List<ProjectDTO> projects;
    private Long totalCount;
    private Integer lastPage;
}
