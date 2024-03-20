package com.axonactive.dojo.department_location.mapper;

import com.axonactive.dojo.base.mapper.BaseMapper;
import com.axonactive.dojo.department_location.dto.DepartmentLocationDTO;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface DepartmentLocationMapper extends BaseMapper<DepartmentLocationDTO, DepartmentLocation> {
}
