package com.axonactive.dojo.project.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.project.entity.Project;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Stateless
public class ProjectDAO extends BaseDAO<Project> {
    public ProjectDAO() {
        super(Project.class);
    }

    public List<Project> findProjectsByDepartmentId (Long departmentId, Integer offset, Integer pageSize) {
        return entityManager.createQuery("select p " +
                        "from Project p " +
                        "where p.department.id = :departmentId and p.status = 'ACTIVE'", Project.class)
                .setParameter("departmentId", departmentId)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Project> findProjects (Integer offset, Integer pageSize) {
        return entityManager.createQuery("select p from Project p where p.status = 'ACTIVE'", Project.class)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public Long findTotalCount() {
        Query query = entityManager.createQuery("select count(p.id) from Project p where p.status = 'ACTIVE'");
        Long count = (Long)query.getSingleResult();
        return count;
    }

    public Long findTotalCountWithDepartmentId(Long departmentId) {
        Query query = entityManager
                .createQuery("select count(p.id) from Project p where p.department.id = :departmentId and p.status = 'ACTIVE'")
                .setParameter("departmentId", departmentId);

        Long count = (Long)query.getSingleResult();
        return count;
    }

    public Optional<Project> findActiveProjectById(Long id) {
        Project project = entityManager.createQuery("select p from Project p " +
                        "where p.id = :id " +
                        "and p.status = 'ACTIVE'", Project.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);

        return Optional.ofNullable(project);
    }

    @Override
    public List<Project> findAll() {
        Query query = entityManager.createQuery("select p from Project p where p.status = 'ACTIVE'", Project.class);

        return query.getResultList();
    }
}
