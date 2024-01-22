package com.axonactive.dojo.employee.entity;

import com.axonactive.dojo.base.entity.BaseEntity;
import com.axonactive.dojo.department.entity.Department;
import lombok.*;
import com.axonactive.dojo.enums.Gender;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Employee extends BaseEntity {

    private String dateOfBirth;
    @NotBlank
    private String firstName;
    private String lastName;
    private String middleName;
    private Integer salary;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    private Department department;

}
