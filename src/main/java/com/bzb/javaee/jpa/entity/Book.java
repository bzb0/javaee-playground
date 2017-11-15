package com.bzb.javaee.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A simple JPA entity that represents a book.
 */
@Entity
@Table
@NamedQueries({@NamedQuery(name = Book.QUERY_FIND_ALL, query = "SELECT b FROM Book b")})
@EqualsAndHashCode(exclude = "id")
@ToString
@Getter
@Setter
public class Book {

  public static final String QUERY_FIND_ALL = "Book.findAll";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "book_id")
  private Long id;

  @Column(length = 50)
  private String name;
}
