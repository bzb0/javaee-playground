package com.bzb.javaee.jpa.entity;

import eu.drus.jpa.unit.api.JpaUnit;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Base class for all JPA entity integration tests.
 *
 * @param <T> The entity type.
 */
@ExtendWith(JpaUnit.class)
public abstract class EntityIT<T> {

  @PersistenceContext(unitName = "integration-test")
  protected EntityManager em;

  /**
   * Starts a resource transaction.
   */
  protected void beginTransaction() {
    em.getTransaction().begin();
  }

  /**
   * Commits the current resource transaction and clears the persistence context.
   */
  protected void commitTransaction() {
    em.getTransaction().commit();
    em.clear();
  }

  /**
   * Roll backs the current resource transaction.
   */
  protected void rollbackTransaction() {
    if (em.getTransaction().isActive()) {
      em.getTransaction().rollback();
    }
  }

  /**
   * Searches for the provided entity type and primary key in the database.
   *
   * @param id The primary key of the entity.
   * @param clazz The entity type.
   * @return The entity wrapped in an {@link Optional} or {@link Optional#empty()} if the entity couldn't be found.
   */
  protected Optional<T> find(Long id, Class<T> clazz) {
    beginTransaction();
    try {
      T entity = em.find(clazz, id);
      commitTransaction();
      return Optional.ofNullable(entity);
    } catch (Throwable e) {
      rollbackTransaction();
      return Optional.empty();
    }
  }

  /**
   * Stores the provided entity in the database.
   *
   * @param entity The entity to be persisted.
   */
  protected void persist(T entity) {
    beginTransaction();
    try {
      em.persist(entity);
      commitTransaction();
    } catch (Throwable e) {
      rollbackTransaction();
    }
  }

  /**
   * Deletes the entity with the provided primary key and type from the database.
   *
   * @param id The primary key of the entity.
   * @param clazz The entity type.
   */
  protected void delete(Long id, Class<T> clazz) {
    beginTransaction();
    try {
      em.remove(em.getReference(clazz, id));
      commitTransaction();
    } catch (Throwable e) {
      rollbackTransaction();
    }
  }
}
