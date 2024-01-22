package com.axonactive.dojo.department.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.project.entity.Project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class DepartmentDAO extends BaseDAO<Department> {
    public DepartmentDAO() {
        super(Department.class);
    }

}
