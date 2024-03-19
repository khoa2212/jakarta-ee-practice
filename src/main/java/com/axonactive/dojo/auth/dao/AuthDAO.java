package com.axonactive.dojo.auth.dao;

import com.axonactive.dojo.base.dao.BaseDAO;
import com.axonactive.dojo.enums.Status;
import com.axonactive.dojo.user.entity.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.Optional;

@Stateless
public class AuthDAO extends BaseDAO<User> {

    public AuthDAO() {
        super(User.class);
    }

    public Optional<User> findActiveUserByEmail(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root);
        query.where(cb.equal(root.get("email"), email), cb.equal(root.get("status"), Status.ACTIVE));

        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }

    public Optional<User> findUserByEmail(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root);
        query.where(cb.equal(root.get("email"), email));

        return entityManager.createQuery(query).getResultList().stream().findFirst();
    }
}
