package com.axonactive.dojo.employee.dto;

import com.axonactive.dojo.assignment.dto.AssignmentDTO;
import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.enums.Gender;
import com.axonactive.dojo.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.*;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private double salary;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Status status;
    private DepartmentDTO department;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AssignmentDTO> assignments;
}
