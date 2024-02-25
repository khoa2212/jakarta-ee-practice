package com.axonactive.dojo.relative.service;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.employee.dao.EmployeeDAO;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.employee.message.EmployeeMessage;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.relative.dao.RelativeDAO;
import com.axonactive.dojo.relative.dto.RelativeListResponseDTO;
import com.axonactive.dojo.relative.entity.Relative;
import com.axonactive.dojo.relative.mapper.RelativeMapper;
import lombok.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class RelativeService {

    @Inject
    private RelativeDAO relativeDAO;

    @Inject
    private EmployeeDAO employeeDAO;

    @Inject
    private RelativeMapper relativeMapper;

    public RelativeListResponseDTO findRelativesByEmployeeId(Long employeeId, Integer pageNumber, Integer pageSize) throws EntityNotFoundException {
        Employee employee = this.employeeDAO
                .findActiveEmployeeById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE));

        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        List<Relative> relatives = this.relativeDAO.findRelativesByEmployeeId(employee.getId(), offset, pageSize);
        Long totalCount = this.relativeDAO.findTotalCount(employee.getId());

        return RelativeListResponseDTO
                .builder()
                .relatives(this.relativeMapper.toListDTO(relatives))
                .totalCount(totalCount)
                .lastPage((totalCount.intValue() / pageSize) + 1)
                .build();
    }
}
