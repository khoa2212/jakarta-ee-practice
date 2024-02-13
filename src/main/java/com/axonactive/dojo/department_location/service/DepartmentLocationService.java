package com.axonactive.dojo.department_location.service;

import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.mapper.DepartmentMapper;
import com.axonactive.dojo.department.message.DepartmentMessage;
import com.axonactive.dojo.department.service.DepartmentService;
import com.axonactive.dojo.department_location.dao.DepartmentLocationDAO;
import com.axonactive.dojo.department_location.dto.*;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;
import com.axonactive.dojo.department_location.mapper.DepartmentLocationMapper;
import com.axonactive.dojo.department_location.message.DepartmentLocationMessage;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
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

        DepartmentLocation newDepartmentLocation = DepartmentLocation
                .builder()
                .location(createDepartmentLocationRequestDTO.getLocation())
                .department(department.get())
                .build();

        DepartmentLocation departmentLocation = this.departmentLocationDAO.add(newDepartmentLocation);

        return this.departmentLocationMapper.toDTO(departmentLocation);
    }

    public DepartmentLocationDTO update(UpdateDepartmentLocationRequestDTO updateDepartmentLocationRequestDTO) throws EntityNotFoundException, BadRequestException {
        Optional<Department> department = this.departmentDAO.findById(updateDepartmentLocationRequestDTO.getDepartmentId());

        if(department.isEmpty()) {
            throw new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT);
        }

        Optional<DepartmentLocation> optionalDepartmentLocation = this.departmentLocationDAO.findById(updateDepartmentLocationRequestDTO.getId());

        if(optionalDepartmentLocation.isEmpty()) {
            throw new EntityNotFoundException(DepartmentLocationMessage.NOT_FOUND_LOCATION);
        }

        Optional<DepartmentLocation> optionalDepartmentLocation1 = this.departmentLocationDAO
                .findDepartmentLocationByDepartmentId(
                        updateDepartmentLocationRequestDTO.getLocation().trim().toLowerCase(),
                        updateDepartmentLocationRequestDTO.getDepartmentId());

        if(optionalDepartmentLocation1.isPresent()) {
            throw new BadRequestException(DepartmentLocationMessage.EXISTED_LOCATION);
        }

        DepartmentLocation departmentLocation = optionalDepartmentLocation.get();

        departmentLocation.setLocation(updateDepartmentLocationRequestDTO.getLocation());
        departmentLocation.setDepartment(department.get());

        DepartmentLocation updatedDepartmentLocation = this.departmentLocationDAO.update(departmentLocation);
        return this.departmentLocationMapper.toDTO(updatedDepartmentLocation);
    }

    public DeleteSuccessMessage delete(DeleteDepartmentLocationRequestDTO deleteDepartmentLocationRequestDTO) throws EntityNotFoundException {
        Optional<DepartmentLocation> optionalDepartmentLocation = this.departmentLocationDAO.findById(deleteDepartmentLocationRequestDTO.getId());

        if(optionalDepartmentLocation.isEmpty()) {
            throw new EntityNotFoundException(DepartmentLocationMessage.NOT_FOUND_LOCATION);
        }

        this.departmentLocationDAO.delete(deleteDepartmentLocationRequestDTO.getId());

        return DepartmentLocationMessage.deleteSuccessMessage();
    }
}
