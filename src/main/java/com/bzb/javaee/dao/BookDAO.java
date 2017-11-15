package com.bzb.javaee.dao;

import com.bzb.javaee.jpa.entity.Book;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * DAO for the {@link Book} entity.
 */
@Stateless
public class BookDAO {

  @PersistenceContext
  EntityManager em;

  /**
   * Stores a new {@link Book} entity with the given name in the database.
   *
   * @param name The name of the book to be saved.
   * @return The persisted book.
   */
  public Book save(String name) {
    Book book = new Book();
    book.setName(name);
    em.persist(book);
    em.flush();
    return book;
  }

  /**
   * Returns the book for the given id from the database.
   *
   * @param id The id of the book.
   * @return The book.
   */
  public Optional<Book> getBook(long id) {
    return Optional.ofNullable(em.find(Book.class, id));
  }

  /**
   * Returns a list of all books stored in the database.
   *
   * @return List of all books.
   */
  public List<Book> getAllBooks() {
    return em.createNamedQuery(Book.QUERY_FIND_ALL, Book.class).getResultList();
  }
}
