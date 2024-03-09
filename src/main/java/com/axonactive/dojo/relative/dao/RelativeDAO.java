package com.axonactive.dojo.relative.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.relative.entity.Relative;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RelativeDAO extends BaseDAO<Relative> {
    public RelativeDAO() {
        super(Relative.class);
    }

    public List<Relative> findRelativesByEmployeeId(long employeeId, int offset, int pageSize) {
        return entityManager.createNamedQuery(Relative.FIND_RELATIVES_BY_EMPLOYEE_ID, Relative.class)
                .setParameter("employeeId", employeeId)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public long findTotalCount(long employeeId) {
        Query query = entityManager.createNamedQuery(Relative.FIND_TOTAL_COUNT);
        query.setParameter("employeeId", employeeId);
        return (long)query.getSingleResult();
    }

    public List<Relative> findRelivesByEmployeesNotAssigned(int offset, int pageSize) {
        EntityGraph<Relative> relativeEntityGraph = entityManager.createEntityGraph(Relative.class);
        relativeEntityGraph.addAttributeNodes("employee");
        relativeEntityGraph.addSubgraph("employee").addAttributeNodes("department");

        TypedQuery<Relative> query =  entityManager.createQuery("select r from Relative r " +
                        "join fetch Employee e on e.id = r.employee.id " +
                        "left join fetch Assignment a on e.id = a.employee.id " +
                        "where a.project.id is null",
                Relative.class).setFirstResult(offset).setMaxResults(pageSize);
        query.setHint("javax.persistence.loadgraph", relativeEntityGraph);

        return query.getResultList();
    }

    public long findTotalCountRelivesByEmployeesNotAssigned() {
        TypedQuery<Long> query =  entityManager.createQuery("select count(r.id) from Relative r " +
                        "join fetch Employee e on e.id = r.employee.id " +
                        "left join fetch Assignment a on e.id = a.employee.id " +
                        "where a.project.id is null",
                Long.class);

        return query.getSingleResult();
    }
}
