package com.axonactive.dojo.relative.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelativeListResponseDTO {
    private List<RelativeDTO> relatives;
    private Long totalCount;
    private Integer lastPage;
}
