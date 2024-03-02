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

    public List<Employee> findEmployeesByDepartmentId (long departmentId, int offset, int pageSize) {

        return entityManager.createNamedQuery(Employee.FIND_EMPLOYEES_BY_DEPARTMENT_ID, Employee.class)
                .setParameter("departmentId", departmentId)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Employee> findEmployees (int offset, int pageSize) {

        return entityManager.createNamedQuery(Employee.FIND_EMPLOYEES, Employee.class)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Employee> findEmployeesByNameAndDepartmentId(long departmentId, int offset, int pageSize, String name) {

        return entityManager.createNamedQuery(Employee.FIND_EMPLOYEES_BY_NAME_AND_DEPARTMENT_ID, Employee.class)
                .setParameter("departmentId", departmentId)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Employee> findEmployeesByName(int offset, int pageSize, String name) {
        return entityManager.createNamedQuery(Employee.FIND_EMPLOYEES_BY_NAME, Employee.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public long findTotalCount() {
        Query query = entityManager.createNamedQuery(Employee.FIND_TOTAL_COUNT);
        return (long)query.getSingleResult();
    }

    public long findTotalCountWithDepartmentId(long departmentId) {
        Query query = entityManager
                .createNamedQuery(Employee.FIND_TOTAL_COUNT_WITH_DEPARTMENT_ID)
                .setParameter("departmentId", departmentId);

        return (long)query.getSingleResult();
    }

    public long findTotalCountWithNameAndDepartmentId(long departmentId, String name) {
        Query query = entityManager
                .createNamedQuery(Employee.FIND_TOTAL_COUNT_WITH_NAME_AND_DEPARTMENT_ID)
                .setParameter("departmentId", departmentId)
                .setParameter("name", "%" + name.toLowerCase() + "%");

        return (long)query.getSingleResult();
    }

    public long findTotalCountWithName(String name) {
        Query query = entityManager
                .createNamedQuery(Employee.FIND_TOTAL_COUNT_WITH_NAME)
                .setParameter("name", "%" + name.toLowerCase() + "%");

        return (long)query.getSingleResult();
    }

    public Optional<Employee> findActiveEmployeeById(long id) {
        return entityManager.createNamedQuery(Employee.FIND_ACTIVE_EMPLOYEE_BY_ID, Employee.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst();
    }
}
