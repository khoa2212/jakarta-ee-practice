package com.axonactive.dojo.department_location.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentLocationListResponseDTO {
    private List<DepartmentLocationDTO> departmentLocations;
    private long totalCount;
    private int lastPage;
}
