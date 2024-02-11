package com.axonactive.dojo.department.mapper;

import com.axonactive.dojo.base.mapper.BaseMapper;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.entity.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface DepartmentMapper extends BaseMapper<DepartmentDTO, Department> {
}
