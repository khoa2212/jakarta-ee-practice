package org.eclipse.jakarta.employee.repository;

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
public class EmployeeRepository extends BaseRepository<Employee, Long> {

    public EmployeeRepository(Class<Employee> entityClass) {
        super(entityClass);
    }
}
