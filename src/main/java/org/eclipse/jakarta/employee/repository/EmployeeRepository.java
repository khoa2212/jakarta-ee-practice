package org.eclipse.jakarta.employee.repository;

import org.eclipse.jakarta.employee.entity.Employee;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class EmployeeRepository {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext
    private EntityManager em;

    public Employee create(Employee emp) {
        logger.info("Creating employee " + emp.getName());
        em.persist(emp);
        return emp;
    }

    public List<Employee> findAll() {
        logger.info("Getting all employee");
        return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }

    public Optional<Employee> findById(Long id) {
        logger.info("Getting employee by id " + id);
        return Optional.ofNullable(em.find(Employee.class, id));
    }

    public void delete(Long id) {
        logger.info("Deleting employee by id " + id);
        var coffee = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid coffee Id:" + id));
        em.remove(coffee);
    }

    public Employee update(Employee employee) {
        logger.info("Updating employee " + employee.getName());
        return em.merge(employee);
    }
}
