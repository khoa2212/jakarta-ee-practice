package com.axonactive.dojo.project.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.department.entity.Department;
import com.axonactive.dojo.employee.entity.Employee;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.project.entity.Project;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Stateless
public class ProjectDAO extends BaseDAO<Project> {
    public ProjectDAO() {
        super(Project.class);
    }

    public List<Project> findProjectsByDepartmentId (long departmentId, int offset, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        root.fetch("department", JoinType.LEFT);

        query.select(root);
        query.where(cb.equal(root.get("department").get("id"), departmentId), cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Project> findProjects (int offset, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        root.fetch("department", JoinType.LEFT);

        query.select(root);
        query.where(cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public long findTotalCount() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Project> root = query.from(Project.class);

        query.select(cb.count(root));
        query.where(cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query).getSingleResult();
    }

    public long findTotalCountWithDepartmentId(long departmentId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Project> root = query.from(Project.class);

        query.select(cb.count(root));
        query.where(cb.equal(root.get("department").get("id"), departmentId), cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query).getSingleResult();
    }

    public Optional<Project> findActiveProjectById(long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        root.fetch("department", JoinType.LEFT);

        query.select(root);
        query.where(cb.equal(root.get("id"), id), cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }

    @Override
    public List<Project> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> query = cb.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);
        root.fetch("department", JoinType.LEFT);

        query.select(root);
        query.where(cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query).getResultList();
    }
}
