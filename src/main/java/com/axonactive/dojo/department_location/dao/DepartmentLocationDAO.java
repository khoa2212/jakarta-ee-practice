package com.axonactive.dojo.department_location.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class DepartmentLocationDAO extends BaseDAO<DepartmentLocation> {
    public DepartmentLocationDAO() {
        super(DepartmentLocation.class);
    }

    public List<DepartmentLocation> findDepartmentLocationByDepartmentId(Long departmentId, Integer pageNumber, Integer pageSize) {
        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        Query query = entityManager.createQuery("select dl from DepartmentLocation dl where dl.department.id = :departmentId", DepartmentLocation.class);
        query.setParameter("departmentId", departmentId);

        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long findTotalCount(Long departmentId) {
        Query query = entityManager.createQuery("select count(dl.id) from DepartmentLocation dl where dl.department.id = :departmentId");
        query.setParameter("departmentId", departmentId);
        Long count = (Long)query.getSingleResult();
        return count;
    }
}
