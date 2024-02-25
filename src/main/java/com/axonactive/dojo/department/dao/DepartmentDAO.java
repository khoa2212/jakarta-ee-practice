package com.axonactive.dojo.department.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.project.entity.Project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class DepartmentDAO extends BaseDAO<Department> {
    public DepartmentDAO() {
        super(Department.class);
    }

    public List<Department> findDepartments(Integer offset, Integer pageSize) {
        Query query = entityManager.createQuery("from Department d where d.status = 'ACTIVE' order by lower(d.departmentName)");
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Long findTotalCount() {
        Query query = entityManager.createQuery("select count(d.id) from Department d where d.status = 'ACTIVE'");
        Long count = (Long)query.getSingleResult();
        return count;
    }

    public Optional<Department> findByDepartmentName(String departmentName) {
        Department department = entityManager
                .createQuery("select d from Department d where lower(d.departmentName) = :departmentName and d.status = 'ACTIVE'", Department.class)
                .setParameter("departmentName", departmentName)
                .getResultList().stream().findFirst().orElse(null);

        return Optional.ofNullable(department);
    }

    public Optional<Department> findActiveDepartmentById(Long id) {
        Department department = entityManager
                .createQuery("select d from Department d where d.id = :id and d.status = 'ACTIVE'", Department.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);

        return Optional.ofNullable(department);
    }

    @Override
    public List<Department> findAll() {
        Query query = entityManager.createQuery("select d from Department d where d.status = 'ACTIVE'", Department.class);

        return query.getResultList();
    }
}
