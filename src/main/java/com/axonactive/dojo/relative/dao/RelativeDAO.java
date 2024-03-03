package com.axonactive.dojo.relative.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;
import com.axonactive.dojo.relative.entity.Relative;

import javax.ejb.Stateless;
import javax.persistence.Query;
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
}
