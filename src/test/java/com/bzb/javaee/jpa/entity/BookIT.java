package com.bzb.javaee.jpa.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import eu.drus.jpa.unit.api.TransactionMode;
import eu.drus.jpa.unit.api.Transactional;
import org.junit.jupiter.api.Test;

@Transactional(TransactionMode.DISABLED)
public class BookIT extends EntityIT<Book> {

  @Test
  public void whenAddBook_thenGetBook() {
    // given
    Book book = new Book();
    book.setName("Roman A");

    // when
    persist(book);

    // then
    assertNotNull(book.getId());
    Book dbBook = find(book.getId(), (Class<Book>) book.getClass())
        .orElseThrow(() -> new NullPointerException("The book could not be found in the DB."));
    assertEquals(book.getName(), dbBook.getName(), "Wrong book name.");
  }

  @Test
  public void whenSetName_thenNameChanged() {
    // given
    Book book = new Book();
    book.setName("Tales");

    // when
    persist(book);

    // then
    assertNotNull(book.getId());

    // when
    beginTransaction();
    book.setName("New Name");
    em.merge(book);
    commitTransaction();

    // then
    Book dbBook = find(book.getId(), (Class<Book>) book.getClass())
        .orElseThrow(() -> new NullPointerException("The book could not be found in the DB."));

    assertEquals("New Name", dbBook.getName(), "The book name was not changed.");
  }
}
