package com.axonactive.dojo.assignment.service;

import com.axonactive.dojo.assignment.dao.AssignmentDAO;
import com.axonactive.dojo.assignment.dto.*;
import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.assignment.mapper.AssignmentMapper;
import com.axonactive.dojo.assignment.message.AssignmentMessage;
import com.axonactive.dojo.base.exception.BadRequestException;
import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.base.message.DeleteSuccessMessage;
import com.axonactive.dojo.employee.dao.EmployeeDAO;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.employee.message.EmployeeMessage;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.project.dao.ProjectDAO;
import com.axonactive.dojo.project.entity.Project;
import com.axonactive.dojo.project.message.ProjectMessage;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
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

        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        if(employeeId > 0) {

            Employee employee = this.employeeDAO
                    .findActiveEmployeeById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE));

            assignments = this.assignmentDAO.findAssignmentsByEmployeeId(employee.getId(), offset, pageSize);
            totalCount = this.assignmentDAO.findTotalCountByEmployeeId(employee.getId());
            assignmentDTOS = this.assignmentMapper.toListDTO(assignments);

            return AssignmentListResponseDTO
                    .builder()
                    .assignments(assignmentDTOS)
                    .totalCount(totalCount)
                    .lastPage((totalCount.intValue() / pageSize) + 1)
                    .build();
        }

        Project project = this.projectDAO
                .findActiveProjectById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(ProjectMessage.NOT_FOUND_PROJECT));

        assignments = this.assignmentDAO.findAssignmentsByProjectId(project.getId(), offset, pageSize);
        totalCount = this.assignmentDAO.findTotalCountByProjectId(project.getId());
        assignmentDTOS = this.assignmentMapper.toListDTO(assignments);

        return AssignmentListResponseDTO
                .builder()
                .assignments(assignmentDTOS)
                .totalCount(totalCount)
                .lastPage((totalCount.intValue() / pageSize) + 1)
                .build();
    }

    public AssignmentDTO add(CreateAssignmentRequestDTO requestDTO) throws EntityNotFoundException, BadRequestException {
        Employee employee = this.employeeDAO
                .findActiveEmployeeById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE));

        Project project = this.projectDAO
                .findActiveProjectById(requestDTO.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException(ProjectMessage.NOT_FOUND_PROJECT));

        Optional<Assignment> optionalAssignment = this.assignmentDAO
                .findAssignmentByEmployeeIdAndProjectId(employee.getId(), project.getId());

        if(optionalAssignment.isPresent()) {
            throw new BadRequestException(AssignmentMessage.EXISTED_ASSIGNMENT);
        }

        Assignment newAssignment = Assignment
                .builder()
                .employee(employee)
                .project(project)
                .numberOfHour(requestDTO.getNumberOfHour())
                .build();

        Assignment assignment = this.assignmentDAO.add(newAssignment);

        return this.assignmentMapper.toDTO(assignment);
    }

    public AssignmentDTO update(UpdateAssignmentRequestDTO requestDTO) throws EntityNotFoundException, BadRequestException {
        Assignment assignment = this.assignmentDAO
                .findById(requestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(AssignmentMessage.NOT_FOUND_ASSIGNMENT));

        Project project = this.projectDAO
                .findActiveProjectById(requestDTO.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException(ProjectMessage.NOT_FOUND_PROJECT));

        Employee employee = this.employeeDAO
                .findActiveEmployeeById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE));

        if(assignment.getEmployee().getId() != employee.getId() ||
                assignment.getProject().getId() != project.getId()) {

            Optional<Assignment> optionalAssignment = this.assignmentDAO
                    .findAssignmentByEmployeeIdAndProjectId(employee.getId(), project.getId());

            if(optionalAssignment.isPresent()) {
                throw new BadRequestException(AssignmentMessage.EXISTED_ASSIGNMENT);
            }
        }

        assignment.setEmployee(employee);
        assignment.setProject(project);
        assignment.setNumberOfHour(requestDTO.getNumberOfHour());

        Assignment updatedAssignment = this.assignmentDAO.update(assignment);

        return this.assignmentMapper.toDTO(updatedAssignment);
    }

    public DeleteSuccessMessage delete(DeleteAssignmentRequestDTO requestDTO) throws EntityNotFoundException {
        Assignment assignment = this.assignmentDAO
                .findById(requestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(AssignmentMessage.NOT_FOUND_ASSIGNMENT));

        this.assignmentDAO.delete(assignment.getId());

        return AssignmentMessage.deleteSuccessMessage();
    }
}
