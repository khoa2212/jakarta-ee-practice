package com.axonactive.dojo.department_location.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DepartmentLocation extends BaseEntity {
    private String location;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentLocation departmentLocation;
}
