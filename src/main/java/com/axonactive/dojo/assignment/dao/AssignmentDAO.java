package com.axonactive.dojo.assignment.dao;

import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;
import com.axonactive.dojo.project.entity.Project;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Stateless
public class AssignmentDAO extends BaseDAO<Assignment> {

    public AssignmentDAO() {
        super(Assignment.class);
    }

    public List<Assignment> findAssignmentsByProjectId(long projectId, int offset, int pageSize) {
        return entityManager.createNamedQuery(Assignment.FIND_ASSIGNMENTS_BY_PROJECT_ID, Assignment.class)
                .setParameter("projectId", projectId)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Assignment> findAssignmentsByEmployeeId(long employeeId, int offset, int pageSize) {
        return entityManager.createNamedQuery(Assignment.FIND_ASSIGNMENTS_BY_EMPLOYEE_ID, Assignment.class)
                .setParameter("employeeId", employeeId)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public Optional<Assignment> findAssignmentByEmployeeIdAndProjectId(long employeeId, long projectId) {
        return entityManager
                .createNamedQuery(Assignment.FIND_ASSIGNMENTS_BY_EMPLOYEE_ID_AND_PROJECT_ID, Assignment.class)
                .setParameter("projectId", projectId)
                .setParameter("employeeId", employeeId)
                .getResultList().stream().findFirst();
    }

    public long findTotalCountByProjectId(long projectId) {
        Query query = entityManager.createNamedQuery(Assignment.FIND_TOTAL_COUNT_BY_PROJECT_ID);
        query.setParameter("projectId", projectId);
        return (long)query.getSingleResult();
    }

    public long findTotalCountByEmployeeId(long employeeId) {
        Query query = entityManager.createNamedQuery(Assignment.FIND_TOTAL_COUNT_BY_EMPLOYEE_ID);
        query.setParameter("employeeId", employeeId);
        return (long)query.getSingleResult();
    }
}
