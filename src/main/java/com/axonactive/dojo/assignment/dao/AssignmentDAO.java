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

    public List<Assignment> findAllAssignmentsByProjectId(Long projectId){
        Query query = entityManager.createQuery("select a from Assignment a where a.project.id = :projectId", Assignment.class);
        query.setParameter("projectId", projectId);

        return query.getResultList();
    }

    public Optional<Assignment> findAssignmentByEmployeeIdAndProjectId(Long employeeId, Long projectId) {
        Query query = entityManager.createQuery("select a from Assignment a where a.project.id = :projectId and a.employee.id = :employeeId", Assignment.class);
        query.setParameter("projectId", projectId);
        query.setParameter("employeeId", employeeId);

        List<Assignment> l = query.getResultList();

        if(l.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(l.get(0));
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
