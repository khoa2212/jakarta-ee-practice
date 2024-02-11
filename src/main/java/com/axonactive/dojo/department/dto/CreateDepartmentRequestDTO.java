package com.axonactive.dojo.department.dto;

import com.axonactive.dojo.base.validations.ValueOfEnum;
import com.axonactive.dojo.enums.Status;
import lombok.*;

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
public class CreateDepartmentRequestDTO {

    @NotBlank(message = "department 's name must not be blank")
    private String departmentName;

    @NotNull(message = "department 's start date is required")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "department 's status is required")
    @ValueOfEnum(enumClass = Status.class)
    private String status;
}
