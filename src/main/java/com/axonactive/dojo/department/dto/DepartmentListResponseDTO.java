package com.axonactive.dojo.department.dto;

import com.axonactive.dojo.department.entity.Department;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DepartmentListResponseDTO {
    List<Department> departmentList;
    Long totalCount;
    Integer lastPage;
}
