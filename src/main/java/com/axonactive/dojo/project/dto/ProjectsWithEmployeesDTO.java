package com.axonactive.dojo.project.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectsWithEmployeesDTO {
    private long id;
    private String projectName;
    private String area;
    private long numberOfEmployees;
    private long totalHours;
    private BigDecimal totalSalaries;
}
