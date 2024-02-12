package com.axonactive.dojo.employee.mapper;

import com.axonactive.dojo.base.mapper.BaseMapper;
import com.axonactive.dojo.employee.dto.EmployeeDTO;
import com.axonactive.dojo.employee.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface EmployeeMapper extends BaseMapper<EmployeeDTO, Employee> {
}
