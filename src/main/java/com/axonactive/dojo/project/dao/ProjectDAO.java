package com.axonactive.dojo.project.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.project.entity.Project;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ProjectDAO extends BaseDAO<Project> {
    public ProjectDAO() {
        super(Project.class);
    }

    public List<Project> findProjectsByDepartmentId (Long departmentId, Integer pageNumber, Integer pageSize) {
        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        return entityManager.createQuery("select p " +
                        "from Project p " +
                        "where p.department.id = :departmentId and p.status = 'ACTIVE'", Project.class)
                .setParameter("departmentId", departmentId)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Project> findProjects (Integer pageNumber, Integer pageSize) {
        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

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
}
