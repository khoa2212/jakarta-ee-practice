package org.eclipse.jakarta.repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface IBaseRepository<T, ID> {

    void setEntityManager(EntityManager entityManager);
    Optional<T> findById(ID id);

    List<T> findAll();

    void create(T entity);

    void update(T entity);

    void delete(T entity);
}
