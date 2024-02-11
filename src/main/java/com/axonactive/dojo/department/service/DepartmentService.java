package com.axonactive.dojo.department.service;

import com.axonactive.dojo.department.dto.DepartmentListResponseDTO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.dao.DepartmentDAO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentService {
    @Inject
    private DepartmentDAO departmentDAO;

    public DepartmentListResponseDTO findDepartments(Integer pageNumber, Integer pageSize) {
        List<Department> departmentList = this.departmentDAO.findDepartments(pageNumber, pageSize);
        Long count = this.departmentDAO.findTotalCount();
        Integer lastPage = (count.intValue() / pageSize) + 1;
        DepartmentListResponseDTO departmentListResponseDTO = DepartmentListResponseDTO
                                                                        .builder()
                                                                        .departmentList(departmentList)
                                                                        .totalCount(count)
                                                                        .lastPage(lastPage)
                                                                        .build();
        return departmentListResponseDTO;
    }

    public Optional<Department> findDepartmentById (Long id) {
        return this.departmentDAO.findById(id);
    }
}
