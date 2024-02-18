package com.axonactive.dojo.department_location.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateDepartmentLocationRequestDTO {

    @NotBlank(message = "Location must not be blank")
    private String location;

    @NotNull(message = "Department's is required")
    private Long departmentId;
}
