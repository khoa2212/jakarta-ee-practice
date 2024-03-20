package com.axonactive.dojo.employee.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.employee.entity.Employee;

import javax.ejb.Stateless;
import javax.persistence.*;
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

    public List<Object[]> findEmployeesByHoursInProject(int offset, int pageSize, int numberOfHour) {
        Query query = entityManager.createQuery("select e, a from Employee e " +
                        "join fetch Assignment a on a.employee.id = e.id " +
                        "join fetch Project p on a.project.id = p.id " +
                        "join fetch Department d on p.department.id = d.id " +
                        "where coalesce(a.numberOfHour) >= :numberOfHour")
                .setParameter("numberOfHour", numberOfHour)
                .setFirstResult(offset)
                .setMaxResults(pageSize);

        return query.getResultList();
    }

    public long findTotalCountEmployeesByHoursInProject(int numberOfHour) {
        return entityManager.createQuery("select count(distinct e.id) from Employee e " +
                        "join Assignment a on a.employee.id = e.id " +
                        "join Project p on a.project.id = p.id " +
                        "join Department d on p.department.id = d.id " +
                        "where coalesce(a.numberOfHour) >= :numberOfHour", Long.class)
                .setParameter("numberOfHour", numberOfHour)
                .getSingleResult();
    }

    public List<Object[]> findEmployeesByHoursInProjectMangedByDepartment(long departmentId, int offset, int pageSize, int numberOfHour) {
        Query query = entityManager.createQuery("select e, a from Employee e " +
                "join fetch Assignment a on a.employee.id = e.id " +
                "join fetch Project p on a.project.id = p.id " +
                "join fetch Department d on p.department.id = d.id " +
                "where d.id = :departmentId and coalesce(a.numberOfHour) >= :numberOfHour")
                .setParameter("departmentId", departmentId)
                .setParameter("numberOfHour", numberOfHour)
                .setFirstResult(offset)
                .setMaxResults(pageSize);

        return query.getResultList();
    }

    public long findTotalCountEmployeesByHoursInProjectMangedByDepartment(long departmentId, int numberOfHour) {
        return entityManager.createQuery("select count(distinct e.id) from Employee e " +
                        "join Assignment a on a.employee.id = e.id " +
                        "join Project p on a.project.id = p.id " +
                        "join Department d on p.department.id = d.id " +
                        "where d.id = :departmentId and coalesce(a.numberOfHour) >= :numberOfHour", Long.class)
                .setParameter("departmentId", departmentId)
                .setParameter("numberOfHour", numberOfHour)
                .getSingleResult();
    }
}
