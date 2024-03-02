package com.axonactive.dojo.department.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.project.entity.Project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class DepartmentDAO extends BaseDAO<Department> {
    public DepartmentDAO() {
        super(Department.class);
    }

    public List<Department> findDepartments(int offset, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> root = query.from(Department.class);
        query.select(root);
        query.where(cb.equal(root.get("status"), Status.ACTIVE));
        List<Order> orderList = new ArrayList<>(List.of(cb.asc(root.get("departmentName"))));
        query.orderBy(orderList);

        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public long findTotalCount() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Department> root = query.from(Department.class);
        query.select(cb.count(root));
        query.where(cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query).getSingleResult();
    }

    public Optional<Department> findByDepartmentName(String departmentName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> root = query.from(Department.class);
        query.select(root);
        query.where(cb.equal(root.get("status"), Status.ACTIVE), cb.equal(root.get("departmentName"), departmentName));

        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }

    public Optional<Department> findActiveDepartmentById(long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> root = query.from(Department.class);
        query.select(root);
        query.where(cb.equal(root.get("status"), Status.ACTIVE), cb.equal(root.get("id"), id));

        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }

    @Override
    public List<Department> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> root = query.from(Department.class);
        query.select(root);
        query.where(cb.equal(root.get("status"), Status.ACTIVE));
        List<Order> orderList = new ArrayList<>(List.of(cb.asc(root.get("departmentName"))));
        query.orderBy(orderList);

        return entityManager.createQuery(query).getResultList();
    }
}
