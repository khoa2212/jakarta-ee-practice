package com.axonactive.dojo.relative.dto;

import com.axonactive.dojo.employee.dto.EmployeeDTO;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.enums.Gender;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelativeDTO {
    private long id;
    private String fullName;
    private Gender gender;
    private String phoneNumber;
    private String relationship;
    private EmployeeDTO employee;
}
