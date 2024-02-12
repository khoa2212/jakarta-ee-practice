package com.axonactive.dojo.assignment.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.project.entity.Project;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "UQ_employee_id_project_id", columnNames = { "employee_id", "project_id" }) })
public class Assignment extends BaseEntity {

    private int numberOfHour;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
