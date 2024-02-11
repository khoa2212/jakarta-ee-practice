package com.axonactive.dojo.department.dto;

import com.axonactive.dojo.department.entity.Department;
import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DepartmentListResponse {
    List<Department> departmentList;
    Long totalCount;
    Integer lastPage;
}
