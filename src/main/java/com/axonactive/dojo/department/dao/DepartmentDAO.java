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

    public List<Department> findDepartments(Integer pageNumber, Integer pageSize) {
        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

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
        Query query = entityManager.createQuery("select d from Department d where lower(d.departmentName) = :departmentName and d.status = 'ACTIVE'", Department.class);
        query.setParameter("departmentName", departmentName);

        List<Department> l = query.getResultList();

        if(l.isEmpty()) {
            return Optional.empty();
        }

        return  Optional.of(l.get(0));
    }

    @Override
    public List<Department> findAll() {
        Query query = entityManager.createQuery("select d from Department d where d.status = 'ACTIVE'", Department.class);

        return query.getResultList();
    }
}
