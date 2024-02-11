package com.axonactive.dojo.department.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Department extends BaseEntity {

    private String departmentName;

    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private Status status;
}
