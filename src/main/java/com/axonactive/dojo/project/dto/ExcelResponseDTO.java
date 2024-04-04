package com.axonactive.dojo.project.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelResponseDTO {
    private String courseTitle;
    private String expiredFrom;
    private String description;
    private int numberOfFemaleCandidate;
    private int numberOfMaleCandidate;

    List<EmployeeResultDTO> employeeResultDTOS;
}
