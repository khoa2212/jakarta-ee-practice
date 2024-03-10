package com.axonactive.dojo.project.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.project.dto.ProjectCountDTO;
import com.axonactive.dojo.project.dto.ProjectsWithEmployeesDTO;
import com.axonactive.dojo.project.entity.Project;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Stateless
public class ProjectDAO extends BaseDAO<Project> {
    public ProjectDAO() {
        super(Project.class);
    }

    public List<Project> findProjectsByDepartmentId (long departmentId, int offset, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        root.fetch("department", JoinType.LEFT);

        query.select(root);
        query.where(cb.equal(root.get("department").get("id"), departmentId), cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Project> findProjects (int offset, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        root.fetch("department", JoinType.LEFT);

        query.select(root);
        query.where(cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public long findTotalCount() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Project> root = query.from(Project.class);

        query.select(cb.count(root));
        query.where(cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query).getSingleResult();
    }

    public long findTotalCountWithDepartmentId(long departmentId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Project> root = query.from(Project.class);

        query.select(cb.count(root));
        query.where(cb.equal(root.get("department").get("id"), departmentId), cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query).getSingleResult();
    }

    public Optional<Project> findActiveProjectById(long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        root.fetch("department", JoinType.LEFT);

        query.select(root);
        query.where(cb.equal(root.get("id"), id), cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }

    @Override
    public List<Project> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        root.fetch("department", JoinType.LEFT);

        query.select(root);
        query.where(cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query).getResultList();
    }

    public List<ProjectsWithEmployeesDTO> findProjectsWithEmployeesSalariesHours(int offset, int pageSize, long numberOfEmployees,
                                                       long totalHours, BigDecimal totalSalaries) {

        return entityManager.createNamedQuery(Project.FIND_PROJECTS_WITH_EMPLOYEES_SALARIES, ProjectsWithEmployeesDTO.class)
                .setParameter("numberOfEmployees", numberOfEmployees)
                .setParameter("totalHours", totalHours)
                .setParameter("totalSalaries", totalSalaries)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public ProjectCountDTO findTotalCountProjectsWithEmployeesSalariesHours(long numberOfEmployees, long totalHours, BigDecimal totalSalaries) {
        return entityManager
                .createNamedQuery(Project.FIND_TOTAL_COUNT_PROJECTS_WITH_EMPLOYEES_SALARIES, ProjectCountDTO.class)
                .setParameter("numberOfEmployees", numberOfEmployees)
                .setParameter("totalHours", totalHours)
                .setParameter("totalSalaries", totalSalaries)
                .getSingleResult();
    }


    public List<ProjectsWithEmployeesDTO> findProjectsWithEmployeesSalariesHours(long numberOfEmployees,
                                                                                 long totalHours, BigDecimal totalSalaries, List<Long> projectIds, String option) {

        switch (option) {
            case "all": {
                return entityManager.createNamedQuery(Project.FIND_PROJECTS_WITH_EMPLOYEES_SALARIES, ProjectsWithEmployeesDTO.class)
                        .setParameter("numberOfEmployees", numberOfEmployees)
                        .setParameter("totalHours", totalHours)
                        .setParameter("totalSalaries", totalSalaries)
                        .getResultList();
            }
            case "in": {
                return entityManager.createNamedQuery(Project.FIND_PROJECTS_WITH_EMPLOYEES_SALARIES_IN_IDS, ProjectsWithEmployeesDTO.class)
                        .setParameter("numberOfEmployees", numberOfEmployees)
                        .setParameter("totalHours", totalHours)
                        .setParameter("totalSalaries", totalSalaries)
                        .setParameter("projectIds", projectIds)
                        .getResultList();
            }
            case "notIn": {
                return entityManager.createNamedQuery(Project.FIND_PROJECTS_WITH_EMPLOYEES_SALARIES_NOT_IN_IDS, ProjectsWithEmployeesDTO.class)
                        .setParameter("numberOfEmployees", numberOfEmployees)
                        .setParameter("totalHours", totalHours)
                        .setParameter("totalSalaries", totalSalaries)
                        .setParameter("projectIds", projectIds)
                        .getResultList();
            }
            default: {
                return entityManager.createNamedQuery(Project.FIND_PROJECTS_WITH_EMPLOYEES_SALARIES, ProjectsWithEmployeesDTO.class)
                        .setParameter("numberOfEmployees", numberOfEmployees)
                        .setParameter("totalHours", totalHours)
                        .setParameter("totalSalaries", totalSalaries)
                        .getResultList();
            }
        }
    }
}
