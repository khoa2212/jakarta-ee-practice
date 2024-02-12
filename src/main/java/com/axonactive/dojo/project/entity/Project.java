package com.axonactive.dojo.project.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.enums.Status;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Project extends BaseEntity {

    private String area;
    private String projectName;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
}
