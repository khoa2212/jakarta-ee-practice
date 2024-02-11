package com.axonactive.dojo.department_location.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentLocationDTO {
    private Long id;
    private String location;
}
