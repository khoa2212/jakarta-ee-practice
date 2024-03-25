package com.axonactive.dojo.department.service;

import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.department.dto.*;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.mapper.DepartmentMapper;
import com.axonactive.dojo.department.message.DepartmentMessage;
import com.axonactive.dojo.enums.Status;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequestScoped
@Transactional
public class DepartmentService {
    @Inject
    private DepartmentDAO departmentDAO;

    @Inject
    private DepartmentMapper departmentMapper;

    public List<DepartmentDTO> findAll() {
        List<Department> departments = this.departmentDAO.findAll();
        return this.departmentMapper.toListDTO(departments);
    }

    public DepartmentListResponseDTO findDepartments(int pageNumber, int pageSize) {
        int offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        List<Department> departments = this.departmentDAO.findDepartments(offset, pageSize);
        List<DepartmentDTO> departmentDTOS = this.departmentMapper.toListDTO(departments);

        long count = this.departmentDAO.findTotalCount();

        int lastPage = ((int) count / pageSize) + 1;
        DepartmentListResponseDTO departmentListResponseDTO = DepartmentListResponseDTO
                .builder()
                .departments(departmentDTOS)
                .totalCount(count)
                .lastPage(lastPage)
                .build();
        return departmentListResponseDTO;
    }

    public DepartmentDTO findById(long id) throws EntityNotFoundException {
        Department department = this.departmentDAO
                .findActiveDepartmentById(id)
                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

        return this.departmentMapper.toDTO(department);
    }

    public DepartmentDTO add(CreateDepartmentRequestDTO createDepartmentRequestDTO) throws BadRequestException {
        Optional<Department> optionalDepartment = this.departmentDAO.findByDepartmentName(
                createDepartmentRequestDTO.getDepartmentName().toLowerCase().trim());

        if (optionalDepartment.isPresent()) {
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

    public DepartmentDTO update(UpdateDepartmentRequestDTO updateDepartmentRequestDTO)
            throws EntityNotFoundException, BadRequestException {
        Department department = this.departmentDAO
                .findActiveDepartmentById(updateDepartmentRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

        Optional<Department> optionalDepartment = this.departmentDAO
                .findByDepartmentName(updateDepartmentRequestDTO.getDepartmentName().toLowerCase().trim());

        if (optionalDepartment.isPresent()
                && !Objects.equals(optionalDepartment.get().getId(), updateDepartmentRequestDTO.getId())) {
            throw new BadRequestException(DepartmentMessage.EXISTED_NAME);
        }

        department.setDepartmentName(updateDepartmentRequestDTO.getDepartmentName());
        department.setStartDate(updateDepartmentRequestDTO.getStartDate());
        department.setStatus(Status.ACTIVE);

        Department updatedDepartment = this.departmentDAO.update(department);

        return this.departmentMapper.toDTO(updatedDepartment);
    }

    public DeleteSuccessMessage deleteSoftly(DeleteDepartmentRequestDTO deleteDepartmentRequestDTO)
            throws EntityNotFoundException {

        Department department = this.departmentDAO
                .findActiveDepartmentById(deleteDepartmentRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT));

        department.setStatus(Status.DELETED);

        this.departmentDAO.update(department);

        return DepartmentMessage.deleteSuccessMessage();
    }
}
