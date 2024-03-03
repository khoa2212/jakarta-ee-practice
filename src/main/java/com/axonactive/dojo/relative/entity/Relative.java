package com.axonactive.dojo.relative.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.enums.Gender;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "relatives")
@NamedQueries({
        @NamedQuery(name = Relative.FIND_RELATIVES_BY_EMPLOYEE_ID, query = "select r from Relative r left join fetch r.employee where r.employee.id = :employeeId"),
        @NamedQuery(name = Relative.FIND_TOTAL_COUNT, query = "select count(r.id) from Relative r where r.employee.id = :employeeId")
})
public class Relative extends BaseEntity {

    public static final String FIND_RELATIVES_BY_EMPLOYEE_ID = "findRelativesByEmployeeId";

    public static final String FIND_TOTAL_COUNT = "Relative.findTotalCount";

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNumber;
    private String relationship;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
