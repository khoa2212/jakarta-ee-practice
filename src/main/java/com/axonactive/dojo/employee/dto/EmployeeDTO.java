package com.axonactive.dojo.employee.dto;

import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.enums.Gender;
import com.axonactive.dojo.enums.Status;
import jakarta.persistence.Column;
import lombok.*;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

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
}
