package com.axonactive.dojo.assignment.dto;

import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.project.entity.Project;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentDTO {
    private Long id;
    private int numberOfHour;
    private Employee employee;
    private Project project;
}
