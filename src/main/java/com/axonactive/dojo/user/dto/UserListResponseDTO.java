package com.axonactive.dojo.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListResponseDTO {
    private List<UserDTO> users;
    private long totalCount;
    private int lastPage;
}
