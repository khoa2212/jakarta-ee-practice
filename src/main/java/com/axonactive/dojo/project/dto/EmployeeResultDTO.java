package com.axonactive.dojo.project.dto;

import com.axonactive.dojo.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResultDTO {
    private String fullName;
    private Gender gender;
    private LocalDate dob;
    private String email;
    private String phone;
    private double points;
}
