package com.axonactive.dojo.project.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.project.dto.ProjectsWithEmployeesDTO;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SqlResultSetMapping(
        name="Project.mapping.findProjectsWithEmployeesSalariesHours",
        classes = @ConstructorResult(targetClass = ProjectsWithEmployeesDTO.class, columns = {
                @ColumnResult(name = "id", type = Long.class),
                @ColumnResult(name = "project_name", type = String.class),
                @ColumnResult(name = "area", type = String.class),
                @ColumnResult(name = "number_of_employees", type = Long.class),
                @ColumnResult(name = "total_hours", type = Long.class),
                @ColumnResult(name = "total_salaries", type = BigDecimal.class)
            }
        )
)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = Project.FIND_PROJECTS_WITH_EMPLOYEES_SALARIES,
                query = "select p.id , p.project_name , p.area, " +
                        "count(e.id) as number_of_employees, " +
                        "coalesce(sum(a.number_of_hour), 0) as total_hours, " +
                        "coalesce(sum(e.salary), 0) as total_salaries " +
                        "from project p left join assignment a on a.project_id = p.id " +
                        "left join employee e on a.employee_id = e.id " +
                        "group by p.id " +
                        "having count(e.id) >= :numberOfEmployees " +
                        "and coalesce(sum(a.number_of_hour), 0) >= :totalHours " +
                        "and coalesce(sum(e.salary), 0) >= :totalSalaries",
                resultSetMapping = "Project.mapping.findProjectsWithEmployeesSalariesHours"
        )
})
public class Project extends BaseEntity {

    public static final String FIND_PROJECTS_WITH_EMPLOYEES_SALARIES = "Project.findProjectsWithEmployeesSalariesHours";

    private String area;
    private String projectName;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;
}
