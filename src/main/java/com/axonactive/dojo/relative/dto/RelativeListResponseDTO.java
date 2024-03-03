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
    private long totalCount;
    private int lastPage;
}
