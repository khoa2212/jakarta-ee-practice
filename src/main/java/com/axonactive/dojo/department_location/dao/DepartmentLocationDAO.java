package com.axonactive.dojo.department_location.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department_location.entity.DepartmentLocation;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartmentLocationDAO extends BaseDAO<DepartmentLocation> {
    public DepartmentLocationDAO() {
        super(DepartmentLocation.class);
    }

    public List<DepartmentLocation> findDepartmentsLocationByDepartmentId(Long departmentId, Integer offset, Integer pageSize) {
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

    public Optional<DepartmentLocation> findDepartmentLocationByDepartmentId(String location, Long departmentId) {
        DepartmentLocation departmentLocation = entityManager
                .createQuery("select dl from DepartmentLocation dl where lower(dl.location) = :location and dl.department.id = :departmentId", DepartmentLocation.class)
                .setParameter("location", location).setParameter("departmentId", departmentId)
                .getResultList().stream().findFirst().orElse(null);

        return Optional.ofNullable(departmentLocation);
    }
}
