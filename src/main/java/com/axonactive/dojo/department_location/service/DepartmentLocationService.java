package com.axonactive.dojo.department_location.service;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.service.DepartmentService;
import com.axonactive.dojo.department_location.dao.DepartmentLocationDAO;
import com.axonactive.dojo.department_location.dto.DepartmentLocationDTO;
import com.axonactive.dojo.department_location.dto.DepartmentLocationListResponseDTO;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;
import com.axonactive.dojo.department_location.mapper.DepartmentLocationMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentLocationService {

    @Inject
    private DepartmentLocationDAO departmentLocationDAO;

    @Inject
    private DepartmentService departmentService;


    @Inject
    private DepartmentLocationMapper departmentLocationMapper;

    public DepartmentLocationListResponseDTO findDepartmentLocationByDepartmentId(Long departmentId, Integer pageNumber, Integer pageSize) throws EntityNotFoundException {
        DepartmentDTO departmentDTO = this.departmentService.findById(departmentId);

        List<DepartmentLocation> departmentLocations =
                this.departmentLocationDAO.findDepartmentLocationByDepartmentId(departmentId, pageNumber, pageSize);

        Long totalCount = this.departmentLocationDAO.findTotalCount(departmentId);

        DepartmentLocationListResponseDTO departmentLocationListResponseDTO = DepartmentLocationListResponseDTO
                .builder()
                .departmentLocations(this.departmentLocationMapper.toListDTO(departmentLocations))
                .totalCount(totalCount)
                .lastPage((totalCount.intValue() / pageSize) + 1)
                .build();

        return departmentLocationListResponseDTO;
    }
}
