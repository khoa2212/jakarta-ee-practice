package com.axonactive.dojo.department_location.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;
import com.axonactive.dojo.enums.Status;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentLocationDAO extends BaseDAO<DepartmentLocation> {
    public DepartmentLocationDAO() {
        super(DepartmentLocation.class);
    }

    public List<DepartmentLocation> findDepartmentsLocationByDepartmentId(long departmentId, int offset, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DepartmentLocation> query = cb.createQuery(DepartmentLocation.class);
        Root<DepartmentLocation> root = query.from(DepartmentLocation.class);

        root.fetch("department", JoinType.LEFT);

        query.select(root);
        query.where(cb.equal(root.get("department").get("id"), departmentId));
        
        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public long findTotalCount(long departmentId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<DepartmentLocation> root = query.from(DepartmentLocation.class);
        query.select(cb.count(root));
        query.where(cb.equal(root.get("department").get("id"), departmentId));

        return entityManager.createQuery(query).getSingleResult();
    }

    public Optional<DepartmentLocation> findDepartmentLocationByDepartmentId(String location, long departmentId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DepartmentLocation> query = cb.createQuery(DepartmentLocation.class);
        Root<DepartmentLocation> root = query.from(DepartmentLocation.class);
        root.fetch("department", JoinType.LEFT);

        query.select(root);
        query.where(cb.equal(root.get("department").get("id"), departmentId), cb.equal(cb.lower(root.get("location")), location.toLowerCase()));

        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }
}
