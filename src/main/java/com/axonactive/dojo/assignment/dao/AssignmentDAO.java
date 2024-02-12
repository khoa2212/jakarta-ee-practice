package com.axonactive.dojo.assignment.dao;

import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;
import com.axonactive.dojo.project.entity.Project;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class AssignmentDAO extends BaseDAO<Assignment> {

    public AssignmentDAO() {
        super(Assignment.class);
    }

    public List<Assignment> findAssignmentsByProjectId(Long projectId, Integer pageNumber, Integer pageSize) {
        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        Query query = entityManager.createQuery("select a from Assignment a where a.project.id = :projectId", Assignment.class);
        query.setParameter("projectId", projectId);

        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public List<Assignment> findAssignmentsByEmployeeId(Long employeeId, Integer pageNumber, Integer pageSize) {
        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        Query query = entityManager.createQuery("select a from Assignment a where a.employee.id = :employeeId", Assignment.class);
        query.setParameter("employeeId", employeeId);

        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long findTotalCountByProjectId(Long projectId) {
        Query query = entityManager.createQuery("select count(a.id) from Assignment a where a.project.id = :projectId");
        query.setParameter("projectId", projectId);
        Long count = (Long)query.getSingleResult();
        return count;
    }

    public Long findTotalCountByEmployeeId(Long employeeId) {
        Query query = entityManager.createQuery("select count(a.id) from Assignment a where a.employee.id = :employeeId");
        query.setParameter("employeeId", employeeId);
        Long count = (Long)query.getSingleResult();
        return count;
    }
}
