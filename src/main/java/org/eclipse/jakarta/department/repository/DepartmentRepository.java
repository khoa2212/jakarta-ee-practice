package org.eclipse.jakarta.department.repository;

import org.eclipse.jakarta.department.entity.Department;
import org.eclipse.jakarta.employee.entity.Employee;
import org.eclipse.jakarta.repository.BaseRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class DepartmentRepository {
    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @PersistenceContext
    private EntityManager em;

    public Department create(Department dep) {
        logger.info("Creating department");
        em.persist(dep);
        return dep;
    }

    public List<Department> findAll() {
        System.out.println("here repository");
        logger.info("Getting all department");
        return em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
    }

    public Optional<Department> findById(Long id) {
        logger.info("Getting department by id " + id);
        return Optional.ofNullable(em.find(Department.class, id));
    }

    public void delete(Long id) {
        logger.info("Deleting department by id " + id);
        var department = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        em.remove(department);
    }

    public Department update(Department department) {
        logger.info("Updating employee");
        return em.merge(department);
    }
}
