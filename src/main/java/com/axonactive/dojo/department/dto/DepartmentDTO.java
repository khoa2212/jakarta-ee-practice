package com.axonactive.dojo.department.dto;

import com.axonactive.dojo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DepartmentDTO {

    private Long id;
    private String departmentName;
    private LocalDate startDate;
    private Status status;
}
