package com.axonactive.dojo.assignment.dao;

import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.base.dao.BaseDAO;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class AssignmentDao extends BaseDAO<Assignment> {

    public AssignmentDao() {
        super(Assignment.class);
    }

//    public List<Assignment> getAssignmentByEmpId(Long empId) {
//        entityManager.createQuery("SELECT a FROM Assignment a WHERE a.employee.id = :")
//    }
}
