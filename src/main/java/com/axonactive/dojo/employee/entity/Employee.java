package com.axonactive.dojo.employee.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.enums.Status;
import lombok.*;
import com.axonactive.dojo.enums.Gender;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import jakarta.persistence.Column;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Employee extends BaseEntity {


    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String middleName;
    private Double salary;

    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
