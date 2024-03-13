package com.axonactive.dojo.user.service;

import com.axonactive.dojo.user.dao.UserDAO;
import com.axonactive.dojo.user.dto.UserDTO;
import com.axonactive.dojo.user.dto.UserListResponseDTO;
import com.axonactive.dojo.user.mapper.UserMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class UserService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserMapper userMapper;

    public UserListResponseDTO findActiveAndInActiveUsers(int pageNumber, int pageSize) {
        int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;
        List<UserDTO> userDTOS = userMapper.toListDTO(userDAO.findActiveAndInActiveUsers(offset, pageSize));
        long totalCount = userDAO.findTotalCountActiveAndInActiveUsers();

        return UserListResponseDTO
                .builder()
                .users(userDTOS)
                .totalCount(totalCount)
                .lastPage(((int)totalCount / pageSize) + 1)
                .build();
    }
}
