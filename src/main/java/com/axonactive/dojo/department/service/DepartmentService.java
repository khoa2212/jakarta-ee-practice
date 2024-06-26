package com.axonactive.dojo.department.service;

import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.department.cache.DepartmentCache;
import com.axonactive.dojo.department.message.DepartmentMessage;
import com.axonactive.dojo.department.dto.*;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.department.mapper.DepartmentMapper;
import com.axonactive.dojo.employee.dto.EmployeeDTO;
import com.axonactive.dojo.enums.Status;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Stateless
public class DepartmentService {
    @Inject
    private DepartmentDAO departmentDAO;

    @Inject
    private DepartmentMapper departmentMapper;

    @Inject
    private DepartmentCache departmentCache;

    public List<DepartmentDTO> findAll() {
        List<Department> departments = departmentCache.getCacheValue("all-departments");

        if(departments == null) {
            departments = this.departmentDAO.findAll();
            departmentCache.setCacheValue("all-departments", departments);
        }

        return this.departmentMapper.toListDTO(departments);
    }

    public DepartmentListResponseDTO findDepartments(int pageNumber, int pageSize) {
        int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        List<Department> departments = this.departmentDAO.findDepartments(offset, pageSize);
        List<DepartmentDTO> departmentDTOS = this.departmentMapper.toListDTO(departments);

        long count = this.departmentDAO.findTotalCount();

        int lastPage = ((int)count/ pageSize) + 1;
        DepartmentListResponseDTO departmentListResponseDTO = DepartmentListResponseDTO
                                                                        .builder()
                                                                        .departments(departmentDTOS)
                                                                        .totalCount(count)
                                                                        .lastPage(lastPage)
                                                                        .build();
        return departmentListResponseDTO;
    }

    public DepartmentDTO findById (long id) throws EntityNotFoundException {
        Department department = this.departmentDAO
                .findActiveDepartmentById(id)
                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

        return this.departmentMapper.toDTO(department);
    }

    public DepartmentDTO add(CreateDepartmentRequestDTO createDepartmentRequestDTO) throws BadRequestException {
        Optional<Department> optionalDepartment = this.departmentDAO.findByDepartmentName(
                createDepartmentRequestDTO.getDepartmentName().toLowerCase().trim());

        if(optionalDepartment.isPresent()) {
            throw new BadRequestException(DepartmentMessage.EXISTED_NAME);
        }

        Department newDepartment = Department.builder()
                .departmentName(createDepartmentRequestDTO.getDepartmentName().trim())
                .startDate(createDepartmentRequestDTO.getStartDate())
                .status(Status.ACTIVE)
                .build();

        Department department = this.departmentDAO.add(newDepartment);

        return this.departmentMapper.toDTO(department);
    }

    public DepartmentDTO update(UpdateDepartmentRequestDTO updateDepartmentRequestDTO) throws EntityNotFoundException, BadRequestException {
        Department department = this.departmentDAO
                .findActiveDepartmentById(updateDepartmentRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

        Optional<Department> optionalDepartment = this.departmentDAO.findByDepartmentName(updateDepartmentRequestDTO.getDepartmentName().toLowerCase().trim());

        if(optionalDepartment.isPresent()
                && !Objects.equals(optionalDepartment.get().getId(), updateDepartmentRequestDTO.getId())) {
            throw new BadRequestException(DepartmentMessage.EXISTED_NAME);
        }

        department.setDepartmentName(updateDepartmentRequestDTO.getDepartmentName());
        department.setStartDate(updateDepartmentRequestDTO.getStartDate());
        department.setStatus(Status.ACTIVE);

        Department updatedDepartment = this.departmentDAO.update(department);

        return this.departmentMapper.toDTO(updatedDepartment);
    }

    public DeleteSuccessMessage deleteSoftly(DeleteDepartmentRequestDTO deleteDepartmentRequestDTO) throws EntityNotFoundException {

        Department department = this.departmentDAO
                .findActiveDepartmentById(deleteDepartmentRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

        department.setStatus(Status.DELETED);

        this.departmentDAO.update(department);

        return DepartmentMessage.deleteSuccessMessage();
    }
}
