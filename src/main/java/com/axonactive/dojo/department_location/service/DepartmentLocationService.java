package com.axonactive.dojo.department_location.service;

import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.mapper.DepartmentMapper;
import com.axonactive.dojo.department.message.DepartmentMessage;
import com.axonactive.dojo.department.service.DepartmentService;
import com.axonactive.dojo.department_location.dao.DepartmentLocationDAO;
import com.axonactive.dojo.department_location.dto.CreateDepartmentLocationRequestDTO;
import com.axonactive.dojo.department_location.dto.DepartmentLocationDTO;
import com.axonactive.dojo.department_location.dto.DepartmentLocationListResponseDTO;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;
import com.axonactive.dojo.department_location.mapper.DepartmentLocationMapper;
import com.axonactive.dojo.department_location.message.DepartmentLocationMessage;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentLocationService {

    @Inject
    private DepartmentLocationDAO departmentLocationDAO;

    @Inject
    private DepartmentDAO departmentDAO;

    @Inject
    private DepartmentLocationMapper departmentLocationMapper;

    @Inject
    private DepartmentMapper departmentMapper;

    public DepartmentLocationListResponseDTO findDepartmentsLocationByDepartmentId(Long departmentId, Integer pageNumber, Integer pageSize) throws EntityNotFoundException {
        Optional<Department> department = this.departmentDAO.findById(departmentId);

        if(department.isEmpty()) {
            throw new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT);
        }

        List<DepartmentLocation> departmentLocations =
                this.departmentLocationDAO.findDepartmentsLocationByDepartmentId(departmentId, pageNumber, pageSize);

        Long totalCount = this.departmentLocationDAO.findTotalCount(departmentId);

        DepartmentLocationListResponseDTO departmentLocationListResponseDTO = DepartmentLocationListResponseDTO
                .builder()
                .departmentLocations(this.departmentLocationMapper.toListDTO(departmentLocations))
                .totalCount(totalCount)
                .lastPage((totalCount.intValue() / pageSize) + 1)
                .build();

        return departmentLocationListResponseDTO;
    }

    public DepartmentLocationDTO add(CreateDepartmentLocationRequestDTO createDepartmentLocationRequestDTO) throws EntityNotFoundException, BadRequestException {
        Optional<Department> department = this.departmentDAO.findById(createDepartmentLocationRequestDTO.getDepartmentId());

        if(department.isEmpty()) {
            throw new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT);
        }

        Optional<DepartmentLocation> optionalDepartmentLocation = this.departmentLocationDAO
                .findDepartmentLocationByDepartmentId(
                        createDepartmentLocationRequestDTO.getLocation().trim().toLowerCase(),
                        createDepartmentLocationRequestDTO.getDepartmentId());

        if(optionalDepartmentLocation.isPresent()) {
            throw new BadRequestException(DepartmentLocationMessage.EXISTED_LOCATION);
        }

        System.out.println("------------------------- " + department.toString() + " --------------------------------");

        DepartmentLocation newDepartmentLocation = DepartmentLocation
                .builder()
                .location(createDepartmentLocationRequestDTO.getLocation())
                .department(department.get())
                .build();

        System.out.println("----------------------------------------------");

        DepartmentLocation departmentLocation = this.departmentLocationDAO.add(newDepartmentLocation);


        DepartmentLocation departmentLocation1 = this.departmentLocationDAO.update(departmentLocation);

        return this.departmentLocationMapper.toDTO(departmentLocation1);
    }
}
