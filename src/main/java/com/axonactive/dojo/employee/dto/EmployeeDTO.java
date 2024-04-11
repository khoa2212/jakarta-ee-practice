package com.axonactive.dojo.employee.dto;

import com.axonactive.dojo.assignment.dto.AssignmentDTO;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.enums.Gender;
import com.axonactive.dojo.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.List;

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


//private Long id;
//
//@JsonbDateFormat(value = "yyyy-MM-dd")
//private LocalDate dateOfBirth;
//
//private String firstName;
//private String lastName;
//private String middleName;
//private int salary;
//private DepartmentDTO department;
//
//@Enumerated(EnumType.STRING)
//private Gender gender;