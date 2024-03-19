package com.axonactive.dojo.assignment.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.project.entity.Project;
import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UQ_employee_id_project_id", columnNames = { "employee_id", "project_id" }) })
@NamedQueries({
        @NamedQuery(name = Assignment.FIND_ASSIGNMENTS_BY_PROJECT_ID, query = "select a from Assignment a left join fetch a.employee left join fetch a.project where a.project.id = :projectId"),
        @NamedQuery(name = Assignment.FIND_ASSIGNMENTS_BY_EMPLOYEE_ID, query = "select a from Assignment a left join fetch a.employee left join fetch a.project where a.employee.id = :employeeId"),
        @NamedQuery(name = Assignment.FIND_ASSIGNMENTS_BY_EMPLOYEE_ID_AND_PROJECT_ID, query = "select a from Assignment a left join fetch a.employee left join fetch a.project where a.project.id = :projectId and a.employee.id = :employeeId"),
        @NamedQuery(name = Assignment.FIND_TOTAL_COUNT_BY_PROJECT_ID, query = "select count(a.id) from Assignment a where a.project.id = :projectId"),
        @NamedQuery(name = Assignment.FIND_TOTAL_COUNT_BY_EMPLOYEE_ID, query = "select count(a.id) from Assignment a where a.employee.id = :employeeId")
})
public class Assignment extends BaseEntity {

    // Named query
    public static final String FIND_ASSIGNMENTS_BY_PROJECT_ID = "findAssignmentsByProjectId";

    public static final String FIND_ASSIGNMENTS_BY_EMPLOYEE_ID = "findAssignmentsByEmployeeId";

    public static final String FIND_ASSIGNMENTS_BY_EMPLOYEE_ID_AND_PROJECT_ID = "findAssignmentByEmployeeIdAndProjectId";

    public static final String FIND_TOTAL_COUNT_BY_PROJECT_ID = "findTotalCountByProjectId";

    public static final String FIND_TOTAL_COUNT_BY_EMPLOYEE_ID = "findTotalCountByEmployeeId";

    // Column
    private int numberOfHour;

    // Relationship
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
