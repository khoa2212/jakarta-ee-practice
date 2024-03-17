package com.axonactive.dojo.assignment.dto;

import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.employee.dto.EmployeeDTO;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.project.dto.ProjectDTO;
import com.axonactive.dojo.project.entity.Project;
import lombok.*;

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

    public AssignmentDTO(long id, int numberOfHour, Employee employee, Project project) {
        this.id = id;
        this.numberOfHour = numberOfHour;

        EmployeeDTO.EmployeeDTOBuilder employeeDTOBuilder = EmployeeDTO.builder();

        employeeDTOBuilder.id(employee.getId());
        employeeDTOBuilder.firstName(employee.getFirstName());
        employeeDTOBuilder.middleName(employee.getMiddleName());
        employeeDTOBuilder.lastName(employee.getLastName());
        employeeDTOBuilder.dateOfBirth(employee.getDateOfBirth());
        employeeDTOBuilder.salary(employee.getSalary());
        employeeDTOBuilder.gender(employee.getGender());
        employeeDTOBuilder.status(employee.getStatus());
        employeeDTOBuilder.department(DepartmentDTO
                .builder()
                .build()
        );

        this.employee = employeeDTOBuilder.build();

        ProjectDTO.ProjectDTOBuilder projectDTOBuilder = ProjectDTO.builder();

        projectDTOBuilder.id(project.getId());
        projectDTOBuilder.projectName(project.getProjectName());
        projectDTOBuilder.area(project.getArea());
        projectDTOBuilder.status(project.getStatus());
        projectDTOBuilder.department(DepartmentDTO.builder().build());

        this.project = projectDTOBuilder.build();
    }
}
