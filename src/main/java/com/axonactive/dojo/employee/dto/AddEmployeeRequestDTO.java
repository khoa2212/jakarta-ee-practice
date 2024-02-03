package com.axonactive.dojo.employee.dto;

import com.axonactive.dojo.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddEmployeeRequestDTO {
    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message = "last name is required")
    private String lastName;

    @NotBlank(message = "gender is required")
    private Gender gender;

    @Null
    private String middleName;

    @Null
    private Double salary;

    private LocalDate dateOfBirth;

    @Null
    private Long departmentId;
}
