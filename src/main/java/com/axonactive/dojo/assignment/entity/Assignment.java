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
@NamedQueries({
        @NamedQuery(name = Assignment.FIND_ASSIGNMENTS_BY_PROJECT_ID, query = "select " +
                "new com.axonactive.dojo.assignment.dto.AssignmentDTO(a.id, a.numberOfHour, a.employee, a.project) " +
                "from Assignment a " +
                "left join fetch Employee e on a.employee.id = e.id " +
                "left join fetch Department d1 on e.department.id = d1.id " +
                "left join fetch Project p on a.project.id = p.id " +
                "left join fetch Department d2 on p.department.id = d2.id " +
                "where a.project.id = :projectId"),
        @NamedQuery(name = Assignment.FIND_ASSIGNMENTS_BY_EMPLOYEE_ID, query = "select a " +
                "from Assignment a left join fetch a.employee e left join fetch e.department d1 left join fetch a.project p left join fetch p.department d2 " +
                "where a.employee.id = :employeeId"),
        @NamedQuery(name = Assignment.FIND_ASSIGNMENTS_BY_EMPLOYEE_ID_AND_PROJECT_ID, query = "select a " +
                "from Assignment a left join fetch a.employee e left join fetch e.department d1 left join fetch a.project p left join fetch p.department d2 " +
                "where a.project.id = :projectId and a.employee.id = :employeeId"),
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
