package com.axonactive.dojo.project.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectsWithEmployeesListDTO {
    private List<ProjectsWithEmployeesDTO> projects;
    private Long totalCount;
    private Integer lastPage;
}
