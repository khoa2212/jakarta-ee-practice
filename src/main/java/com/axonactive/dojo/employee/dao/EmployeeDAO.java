package com.axonactive.dojo.employee.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.employee.entity.Employee;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.Null;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
@Stateless
public class EmployeeDAO extends BaseDAO<Employee> {
    public EmployeeDAO() {
        super(Employee.class);
    }

    public List<Employee> findEmployeesByDepartmentId (Long departmentId, Integer pageNumber, Integer pageSize) {
        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        return entityManager.createQuery("select e " +
                "from Employee e " +
                "where e.department.id = :departmentId and e.status = 'ACTIVE'", Employee.class)
                .setParameter("departmentId", departmentId)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Employee> findEmployees (Integer pageNumber, Integer pageSize) {
        Integer offset = (pageNumber <= 1 ? 0 : pageNumber - 1) * pageSize;

        return entityManager.createQuery("select e from Employee e where e.status = 'ACTIVE'", Employee.class)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public Long findTotalCount() {
        Query query = entityManager.createQuery("select count(e.id) from Employee e where e.status = 'ACTIVE'");
        Long count = (Long)query.getSingleResult();
        return count;
    }

    public Long findTotalCountWithDepartmentId(Long departmentId) {
        Query query = entityManager
                .createQuery("select count(e.id) from Employee e where e.department.id = :departmentId and e.status = 'ACTIVE'")
                .setParameter("departmentId", departmentId);

        Long count = (Long)query.getSingleResult();
        return count;
    }
}
