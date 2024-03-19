package com.axonactive.dojo.assignment.dto;

import com.axonactive.dojo.employee.dto.EmployeeDTO;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.project.dto.ProjectDTO;
import com.axonactive.dojo.project.entity.Project;
import lombok.*;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentDTO {
    private long id;
    private int numberOfHour;
    private EmployeeDTO employee;
    private ProjectDTO project;
}
