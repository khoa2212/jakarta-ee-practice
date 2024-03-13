package com.axonactive.dojo.user.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.user.entity.User;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class UserDAO extends BaseDAO<User> {
    public UserDAO() {
        super(User.class);
    }

    public List<User> findActiveAndInActiveUsers(int offset, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root);
        query.where(cb.or(
                cb.equal(root.get("status"), Status.ACTIVE),
                cb.equal(root.get("status"), Status.INACTIVE)
        ));

        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize).getResultList();
    }

    public long findTotalCountActiveAndInActiveUsers() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        query.select(cb.count(root));
        query.where(cb.or(
                cb.equal(root.get("status"), Status.ACTIVE),
                cb.equal(root.get("status"), Status.INACTIVE)
        ));

        return entityManager.createQuery(query).getSingleResult();
    }
}
