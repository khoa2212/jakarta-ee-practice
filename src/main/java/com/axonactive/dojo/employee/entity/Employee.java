package com.axonactive.dojo.employee.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.enums.Status;
import lombok.*;
import com.axonactive.dojo.enums.Gender;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;
import jakarta.persistence.Column;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@NamedQueries({
                @NamedQuery(name = Employee.FIND_EMPLOYEES_BY_DEPARTMENT_ID, query = "select e " +
                                "from Employee e left join fetch e.department " +
                                "where e.department.id = :departmentId and e.status = 'ACTIVE'"),

                @NamedQuery(name = Employee.FIND_EMPLOYEES, query = "select e " +
                                "from Employee e left join fetch e.department " +
                                "where e.status = 'ACTIVE'"),

                @NamedQuery(name = Employee.FIND_EMPLOYEES_BY_NAME_AND_DEPARTMENT_ID, query = "select e " +
                                "from Employee e left join fetch e.department " +
                                "where e.department.id = :departmentId and e.status = 'ACTIVE' " +
                                "and (lower(concat(e.firstName, ' ', e.lastName)) like :name or lower(concat(e.middleName, ' ', e.firstName, ' ', e.lastName)) like :name)"),

                @NamedQuery(name = Employee.FIND_EMPLOYEES_BY_NAME, query = "select e " +
                                "from Employee e left join fetch e.department " +
                                "where e.status = 'ACTIVE' " +
                                "and (lower(concat(e.firstName, ' ', e.lastName)) like :name or lower(concat(e.middleName, ' ', e.firstName, ' ', e.lastName)) like :name)"),

                @NamedQuery(name = Employee.FIND_TOTAL_COUNT, query = "select count(e.id) " +
                                "from Employee e " +
                                "where e.status = 'ACTIVE'"),

                @NamedQuery(name = Employee.FIND_TOTAL_COUNT_WITH_DEPARTMENT_ID, query = "select count(e.id) " +
                                "from Employee e " +
                                "where e.department.id = :departmentId and e.status = 'ACTIVE'"),

                @NamedQuery(name = Employee.FIND_TOTAL_COUNT_WITH_NAME_AND_DEPARTMENT_ID, query = "select count(e.id) "
                                +
                                "from Employee e " +
                                "where e.department.id = :departmentId and e.status = 'ACTIVE' " +
                                "and (lower(concat(e.firstName, ' ', e.lastName)) like :name or lower(concat(e.middleName, ' ', e.firstName, ' ', e.lastName)) like :name)"),

                @NamedQuery(name = Employee.FIND_TOTAL_COUNT_WITH_NAME, query = "select count(e.id) " +
                                "from Employee e " +
                                "where e.status = 'ACTIVE' " +
                                "and (lower(concat(e.firstName, ' ', e.lastName)) like :name or lower(concat(e.middleName, ' ', e.firstName, ' ', e.lastName)) like :name)"),

                @NamedQuery(name = Employee.FIND_ACTIVE_EMPLOYEE_BY_ID, query = "select e " +
                                "from Employee e left join fetch e.department " +
                                "where e.id = :id and e.status = 'ACTIVE'")

})
public class Employee extends BaseEntity {

        // Named query
        public static final String FIND_EMPLOYEES_BY_DEPARTMENT_ID = "findEmployeesByDepartmentId";

        public static final String FIND_EMPLOYEES = "findEmployees";

        public static final String FIND_EMPLOYEES_BY_NAME_AND_DEPARTMENT_ID = "findEmployeesByNameAndDepartmentId";

        public static final String FIND_EMPLOYEES_BY_NAME = "findEmployeesByName";

        public static final String FIND_TOTAL_COUNT = "Employee.findTotalCount";

        public static final String FIND_TOTAL_COUNT_WITH_DEPARTMENT_ID = "findTotalCountWithDepartmentId";

        public static final String FIND_TOTAL_COUNT_WITH_NAME_AND_DEPARTMENT_ID = "findTotalCountWithNameAndDepartmentId";

        public static final String FIND_TOTAL_COUNT_WITH_NAME = "findTotalCountWithName";

        public static final String FIND_ACTIVE_EMPLOYEE_BY_ID = "findActiveEmployeeById";

        // Column
        @Column(nullable = false)
        private String firstName;

        @Column(nullable = false)
        private String lastName;

        private String middleName;
        private double salary;

        @JsonbDateFormat("yyyy-MM-dd")
        private LocalDate dateOfBirth;

        @Enumerated(EnumType.STRING)
        private Gender gender;

        @Enumerated(EnumType.STRING)
        private Status status;

        // Relationship
        @ManyToOne
        @JoinColumn(name = "department_id")
        private Department department;

}
