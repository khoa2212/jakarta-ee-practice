package com.axonactive.dojo.project.service;

import com.axonactive.dojo.base.exception.EntityNotFoundException;
import com.axonactive.dojo.department.dao.DepartmentDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department.message.DepartmentMessage;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.project.dao.ProjectDAO;
import com.axonactive.dojo.project.dto.ProjectDTO;
import com.axonactive.dojo.project.dto.ProjectListResponseDTO;
import com.axonactive.dojo.project.entity.Project;
import com.axonactive.dojo.project.mapper.ProjectMapper;
import com.axonactive.dojo.project.message.ProjectMessage;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class ProjectService {

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private DepartmentDAO departmentDAO;

    @Inject
    private ProjectMapper projectMapper;

    public List<ProjectDTO> findAll() {
        List<Project> projects = this.projectDAO.findAll();
        return this.projectMapper.toListDTO(projects);
    }

    public ProjectListResponseDTO findProjects(Long departmentId, Integer pageNumber, Integer pageSize) throws EntityNotFoundException {
        List<Project> projects;
        List<ProjectDTO> projectDTOS;
        Long totalCount = 0L;

        if(departmentId > 0) {
            Optional<Department> department = this.departmentDAO.findById(departmentId);

            if(department.isEmpty() || department.get().getStatus() == Status.DELETED) {
                throw new EntityNotFoundException(DepartmentMessage.NOT_FOUND_DEPARTMENT);
            }

            projects = this.projectDAO.findProjectsByDepartmentId(departmentId, pageNumber, pageSize);
            projectDTOS = this.projectMapper.toListDTO(projects);
            totalCount = this.projectDAO.findTotalCountWithDepartmentId(departmentId);
            return ProjectListResponseDTO
                    .builder()
                    .projects(projectDTOS)
                    .totalCount(totalCount)
                    .lastPage((totalCount.intValue() / pageSize) + 1)
                    .build();
        }

        projects = this.projectDAO.findProjects(pageNumber, pageSize);
        projectDTOS = this.projectMapper.toListDTO(projects);
        totalCount = this.projectDAO.findTotalCount();

        return ProjectListResponseDTO
                .builder()
                .projects(projectDTOS)
                .totalCount(totalCount)
                .lastPage((totalCount.intValue() / pageSize) + 1)
                .build();
    }

    public ProjectDTO findById(Long id) throws EntityNotFoundException {
        Optional<Project> optionalProject = this.projectDAO.findById(id);

        if(optionalProject.isEmpty() || optionalProject.get().getStatus() == Status.DELETED) {
            throw new EntityNotFoundException(ProjectMessage.NOT_FOUND_PROJECT);
        }

        return this.projectMapper.toDTO(optionalProject.get());
    }
}
