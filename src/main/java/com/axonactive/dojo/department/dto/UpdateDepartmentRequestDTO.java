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
public class UpdateDepartmentRequestDTO {
    @NotNull(message = "Department's id is required")
    private Long id;

    @NotBlank(message = "Department 's name must not be blank")
    private String departmentName;

    @NotNull(message = "Department 's start date is required")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate startDate;
}
