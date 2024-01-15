package org.eclipse.jakarta.department.entity;

import lombok.*;
import org.eclipse.jakarta.employee.entity.Employee;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
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
    @JsonbProperty("id")
    private Long id;

    @Column(name = "department_name")
    @JsonbProperty("departmentName")
    private String departmentName;

    @Column(name = "start_date")
    @JsonbDateFormat("yyyy-MM-dd")
    @JsonbProperty("startDate")
    private LocalDate startDate;

//    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
//    @JsonbTransient()
//    private List<Employee> employees;
}
