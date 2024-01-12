package org.eclipse.jakarta.department.repository;

import org.eclipse.jakarta.department.entity.Department;
import org.eclipse.jakarta.repository.BaseRepository;

import javax.ejb.Stateless;

@Stateless
public class DepartmentRepository extends BaseRepository<Department, Long> {
    public DepartmentRepository(Class<Department> entityClass) {
        super(entityClass);
    }
}
