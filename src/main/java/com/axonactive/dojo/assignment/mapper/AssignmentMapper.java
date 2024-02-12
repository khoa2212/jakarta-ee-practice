package com.axonactive.dojo.assignment.mapper;

import com.axonactive.dojo.assignment.dto.AssignmentDTO;
import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.base.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface AssignmentMapper extends BaseMapper<AssignmentDTO, Assignment> {
}
