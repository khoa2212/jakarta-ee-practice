package com.axonactive.dojo.assignment.service;

import com.axonactive.dojo.assignment.dao.AssignmentDAO;
import com.axonactive.dojo.assignment.dto.*;
import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.assignment.mapper.AssignmentMapper;
import com.axonactive.dojo.assignment.message.AssignmentMessage;
import com.axonactive.dojo.base.exception.BadRequestException;
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

    public AssignmentDTO add(CreateAssignmentRequestDTO requestDTO) throws EntityNotFoundException, BadRequestException {
        Optional<Employee> optionalEmployee = this.employeeDAO.findById(requestDTO.getEmployeeId());

        if(optionalEmployee.isEmpty() || optionalEmployee.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE);
        }

        Optional<Project> optionalProject = this.projectDAO.findById(requestDTO.getProjectId());

        if(optionalProject.isEmpty() || optionalProject.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(ProjectMessage.NOT_FOUND_PROJECT);
        }

        Optional<Assignment> optionalAssignment = this.assignmentDAO
                .findAssignmentByEmployeeIdAndProjectId(requestDTO.getEmployeeId(), requestDTO.getProjectId());

        if(optionalAssignment.isPresent()) {
            throw new BadRequestException(AssignmentMessage.EXISTED_ASSIGNMENT);
        }

        Assignment newAssignment = Assignment
                .builder()
                .employee(optionalEmployee.get())
                .project(optionalProject.get())
                .numberOfHour(requestDTO.getNumberOfHour())
                .build();

        Assignment assignment = this.assignmentDAO.add(newAssignment);

        return this.assignmentMapper.toDTO(assignment);
    }

    public AssignmentDTO update(UpdateAssignmentRequestDTO requestDTO) throws EntityNotFoundException, BadRequestException {
        Optional<Assignment> optionalAssignment = this.assignmentDAO.findById(requestDTO.getId());

        if (optionalAssignment.isEmpty()) {
            throw new EntityNotFoundException(AssignmentMessage.NOT_FOUND_ASSIGNMENT);
        }

        Optional<Employee> optionalEmployee = this.employeeDAO.findById(requestDTO.getEmployeeId());

        if(optionalEmployee.isEmpty() || optionalEmployee.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(EmployeeMessage.NOT_FOUND_EMPLOYEE);
        }

        Optional<Project> optionalProject = this.projectDAO.findById(requestDTO.getProjectId());

        if(optionalProject.isEmpty() || optionalProject.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(ProjectMessage.NOT_FOUND_PROJECT);
        }

        Assignment assignment = optionalAssignment.get();

        if(assignment.getEmployee().getId() != requestDTO.getEmployeeId() ||
                assignment.getProject().getId() != requestDTO.getProjectId()) {

            Optional<Assignment> optionalAssignment1 = this.assignmentDAO
                    .findAssignmentByEmployeeIdAndProjectId(requestDTO.getEmployeeId(), requestDTO.getProjectId());

            if(optionalAssignment1.isPresent()) {
                throw new BadRequestException(AssignmentMessage.EXISTED_ASSIGNMENT);
            }
        }

        assignment.setEmployee(optionalEmployee.get());
        assignment.setProject(optionalProject.get());
        assignment.setNumberOfHour(requestDTO.getNumberOfHour());

        Assignment updatedAssignment = this.assignmentDAO.add(assignment);

        return this.assignmentMapper.toDTO(updatedAssignment);
    }

    public JsonObject delete(DeleteAssignmentRequestDTO requestDTO) throws EntityNotFoundException {
        Optional<Assignment> optionalAssignment = this.assignmentDAO.findById(requestDTO.getId());

        if (optionalAssignment.isEmpty()) {
            throw new EntityNotFoundException(AssignmentMessage.NOT_FOUND_ASSIGNMENT);
        }

        this.assignmentDAO.delete(requestDTO.getId());

        return AssignmentMessage.deleteSuccessMessage();
    }

//    public List<AssignmentDTO> findAllAssignmentsByProjectId(Long projectId) throws EntityNotFoundException {
//        Optional<Project> optionalProject = this.projectDAO.findById(projectId);
//
//        if(optionalProject.isEmpty() || optionalProject.get().getStatus() == Status.DELETED) {
//            throw new EntityNotFoundException(ProjectMessage.NOT_FOUND_PROJECT);
//        }
//
//        List<Assignment> assignments = this.assignmentDAO.findAllAssignmentsByProjectId(projectId);
//
//        return this.assignmentMapper.toListDTO(assignments);
//    }
}
