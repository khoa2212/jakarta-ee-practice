package com.axonactive.dojo.project.dto;

import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.entity.Department;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long id;
    private String projectName;
    private String area;
    private Department department;
}
