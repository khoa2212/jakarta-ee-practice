package org.eclipse.jakarta.department.entity;

import lombok.*;
import org.eclipse.jakarta.employee.entity.Employee;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "department")
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "start_date")
    private LocalDate startDate;

//    @OneToMany(mappedBy = "department")
//    private List<Employee> employees;
}
