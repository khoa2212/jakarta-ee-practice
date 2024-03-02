package com.axonactive.dojo.employee.dto;

import com.axonactive.dojo.base.validations.ValueOfEnum;
import com.axonactive.dojo.enums.Gender;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateEmployeeRequestDTO {
    @NotNull(message = "Employee's id is required")
    private Long id;

    @NotNull(message = "Employee's first name is required")
    private String firstName;

    @NotNull(message = "Employee's last name is required")
    private String lastName;

    @NotNull(message = "Employee's gender is required")
    @ValueOfEnum(enumClass = Gender.class)
    private String gender;

    @Nullable
    private String middleName;

    private double salary;

    @NotNull(message = "Employee's date of birth is required")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull(message = "Department's id is required")
    private Long departmentId;
}
