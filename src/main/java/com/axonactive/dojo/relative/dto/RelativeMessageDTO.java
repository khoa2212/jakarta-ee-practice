package com.axonactive.dojo.relative.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelativeMessageDTO {
    private Long id;
    private String fullName;
    private String gender;
    private String phoneNumber;
    private String relationship;
    private Long employeeId;
}
