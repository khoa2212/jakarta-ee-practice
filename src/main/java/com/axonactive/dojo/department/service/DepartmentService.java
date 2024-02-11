package com.axonactive.dojo.department.service;

import com.axonactive.dojo.department.dto.CreateDepartmentRequestDTO;
import com.axonactive.dojo.department.dto.DepartmentDTO;
import com.axonactive.dojo.department.dto.DepartmentListResponseDTO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.department.mapper.DepartmentMapper;
import com.axonactive.dojo.enums.Status;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentService {
    @Inject
    private DepartmentDAO departmentDAO;

    @Inject
    private DepartmentMapper departmentMapper;

    public DepartmentListResponseDTO findDepartments(Integer pageNumber, Integer pageSize) {
        List<Department> departments = this.departmentDAO.findDepartments(pageNumber, pageSize);
        List<DepartmentDTO> departmentDTOS = this.departmentMapper.toListDTO(departments);

        Long count = this.departmentDAO.findTotalCount();

        Integer lastPage = (count.intValue() / pageSize) + 1;
        DepartmentListResponseDTO departmentListResponseDTO = DepartmentListResponseDTO
                                                                        .builder()
                                                                        .departments(departmentDTOS)
                                                                        .totalCount(count)
                                                                        .lastPage(lastPage)
                                                                        .build();
        return departmentListResponseDTO;
    }

    public Optional<Department> findById (Long id) {
        return this.departmentDAO.findById(id);
    }

    public DepartmentDTO add(CreateDepartmentRequestDTO createDepartmentRequestDTO) {
        Department newDepartment = Department.builder()
                .departmentName(createDepartmentRequestDTO.getDepartmentName())
                .startDate(createDepartmentRequestDTO.getStartDate())
                .status(Status.valueOf(createDepartmentRequestDTO.getStatus()))
                .build();

        Department department = this.departmentDAO.add(newDepartment);

        return this.departmentMapper.toDTO(department);
    }
}
