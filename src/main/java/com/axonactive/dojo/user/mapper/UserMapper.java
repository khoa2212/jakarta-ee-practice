package com.axonactive.dojo.user.mapper;

import com.axonactive.dojo.base.mapper.BaseMapper;
import com.axonactive.dojo.user.dto.UserDTO;
import com.axonactive.dojo.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface UserMapper extends BaseMapper<UserDTO, User> {
}
