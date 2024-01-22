package com.axonactive.dojo.employee.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.employee.entity.Employee;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public List<Employee> getEmployeeByDepartmentID (Long departmentID) {
        return entityManager.createQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE e.department.id = :departmentID", Employee.class)
                .setParameter("departmentID", departmentID)
                .getResultList();
    }

//    public List<Employee> get()
}
