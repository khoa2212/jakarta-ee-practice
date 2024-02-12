package com.axonactive.dojo.assignment.service;

import com.axonactive.dojo.assignment.dao.AssignmentDAO;
import com.axonactive.dojo.assignment.dto.AssignmentDTO;
import com.axonactive.dojo.assignment.dto.AssignmentListResponseDTO;
import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.assignment.mapper.AssignmentMapper;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.employee.dao.EmployeeDAO;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.employee.message.EmployeeMessage;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.project.dao.ProjectDAO;
import com.axonactive.dojo.project.entity.Project;
import com.axonactive.dojo.project.message.ProjectMessage;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class AssignmentService {
    @Inject
    private AssignmentDAO assignmentDAO;

    @Inject
    private AssignmentMapper assignmentMapper;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private EmployeeDAO employeeDAO;

    public AssignmentListResponseDTO findAssignments(Long projectId, Long employeeId, Integer pageNumber, Integer pageSize) throws EntityNotFoundException {
        List<Assignment> assignments;
        List<AssignmentDTO> assignmentDTOS;
        Long totalCount = 0L;

        if(employeeId > 0) {

            Optional<Employee> optionalEmployee = this.employeeDAO.findById(employeeId);

            if(optionalEmployee.isEmpty() || optionalEmployee.get().getStatus() == Status.DELETED) {
                throw new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE);
            }

            assignments = this.assignmentDAO.findAssignmentsByEmployeeId(employeeId, pageNumber, pageSize);
            totalCount = this.assignmentDAO.findTotalCountByEmployeeId(employeeId);
            assignmentDTOS = this.assignmentMapper.toListDTO(assignments);

            return AssignmentListResponseDTO
                    .builder()
                    .assignments(assignmentDTOS)
                    .totalCount(totalCount)
                    .lastPage((totalCount.intValue() / pageSize) + 1)
                    .build();
        }

        Optional<Project> optionalProject = this.projectDAO.findById(projectId);

        if(optionalProject.isEmpty() || optionalProject.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(ProjectMessage.NOT_FOUND_PROJECT);
        }

        assignments = this.assignmentDAO.findAssignmentsByProjectId(projectId, pageNumber, pageSize);
        totalCount = this.assignmentDAO.findTotalCountByProjectId(projectId);
        assignmentDTOS = this.assignmentMapper.toListDTO(assignments);

        return AssignmentListResponseDTO
                .builder()
                .assignments(assignmentDTOS)
                .totalCount(totalCount)
                .lastPage((totalCount.intValue() / pageSize) + 1)
                .build();
    }
}
