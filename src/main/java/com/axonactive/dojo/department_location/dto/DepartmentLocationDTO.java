package com.axonactive.dojo.department_location.dto;


import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.entity.Department;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentLocationDTO {
    private long id;
    private String location;
    private DepartmentDTO department;
}
