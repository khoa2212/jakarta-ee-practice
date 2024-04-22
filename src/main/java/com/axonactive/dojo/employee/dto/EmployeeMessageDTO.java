package com.axonactive.dojo.employee.dto;

import com.axonactive.dojo.assignment.dto.AssignmentDTO;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.enums.Gender;
import com.axonactive.dojo.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeMessageDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private double salary;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Status status;
    private Long departmentId;
}
