package com.axonactive.dojo.department_location.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.department.entity.Department;
import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DepartmentLocation extends BaseEntity {
    private String location;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
