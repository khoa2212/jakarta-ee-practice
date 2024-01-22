package com.axonactive.dojo.assignment.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.project.entity.Project;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Assignment extends BaseEntity {

    private int numberOfHours;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Project project;
}
