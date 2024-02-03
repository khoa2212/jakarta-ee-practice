package com.axonactive.dojo.assignment.dao;

import com.axonactive.dojo.assignment.entity.Assignment;
import com.axonactive.dojo.base.dao.BaseDAO;

import javax.ejb.Stateless;

@Stateless
public class AssignmentDAO extends BaseDAO<Assignment> {

    public AssignmentDAO() {
        super(Assignment.class);
    }

//    public List<Assignment> getAssignmentByEmpId(Long empId) {
//        entityManager.createQuery("SELECT a FROM Assignment a WHERE a.employee.id = :")
//    }
}
