package com.axonactive.dojo.employee.dto;

import com.axonactive.dojo.base.validations.ValueOfEnum;
import com.axonactive.dojo.enums.Gender;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateEmployeeRequestDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Gender is required")
    @ValueOfEnum(enumClass = Gender.class)
    private String gender;

    @Nullable
    private String middleName;

    @Nullable
    private Double salary;

    @NotNull(message = "Date of birth is required")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull(message = "Department's id is required")
    private Long departmentId;
}
