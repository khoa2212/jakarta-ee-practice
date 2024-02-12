package com.axonactive.dojo.employee.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeListResponseDTO {
    private List<EmployeeDTO> employees;
    private Long totalCount;
    private Integer lastPage;
}
