package com.bzb.javaee.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * A JPA entity, that represents a member of a {@link Family}.
 */
@Entity
@Table
@NamedQueries({@NamedQuery(name = Person.QUERY_FIND_ALL, query = "SELECT p FROM Person p")})
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id"})
public class Person {

  public static final String QUERY_FIND_ALL = "Person.findAll";

  @Version
  private int version;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(length = 40)
  private String firstName;

  @Column(length = 40)
  private String lastName;

  @ManyToOne
  @JoinColumn(name = "FAMILY_ID")
  private Family family;

}
