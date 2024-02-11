package com.axonactive.dojo.department.mapper;

import com.axonactive.dojo.base.mapper.BaseMapper;
import com.axonactive.dojo.department.dto.CreateDepartmentRequestDTO;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface DepartmentMapper extends BaseMapper<DepartmentDTO, Department> {

//    Department convertCreateRequestToEntity(CreateDepartmentRequestDTO createDepartmentRequestDTO);
}
