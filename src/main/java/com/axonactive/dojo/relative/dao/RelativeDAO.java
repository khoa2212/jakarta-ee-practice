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

    public List<Relative> findRelativesByEmployeeId(Long employeeId, Integer pageNumber, Integer pageSize) {
        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        Query query = entityManager.createQuery("select r from Relative r where r.employee.id = :employeeId", Relative.class);
        query.setParameter("employeeId", employeeId);

        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long findTotalCount(Long employeeId) {
        Query query = entityManager.createQuery("select count(r.id) from Relative r where r.employee.id = :employeeId");
        query.setParameter("employeeId", employeeId);
        Long count = (Long)query.getSingleResult();
        return count;
    }
}
