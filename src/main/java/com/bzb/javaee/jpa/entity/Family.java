package com.bzb.javaee.jpa.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A JPA entity, that represents a family.
 */
@Entity
@Table
@EqualsAndHashCode(exclude = "id")
@ToString
@Getter
@Setter
public class Family {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "family_id")
  private int id;

  @Column(length = 30)
  private String description;

  @OneToMany(mappedBy = "family", cascade = {CascadeType.ALL})
  private List<Person> persons = new ArrayList<>();

  /**
   * Adds a {@link Person} to the family.
   *
   * @param person The person object.
   */
  public void addMember(Person person) {
    Objects.requireNonNull(person, "Person must not be null.");

    /* If the person is already part of a family, we delete the old family. */
    Optional.ofNullable(person.getFamily()).ifPresent(family -> family.getPersons().remove(person));

    /* http://stackoverflow.com/questions/2521659/foreign-key-not-stored-in-child-entity-one-to-many
     * www.java2s.com/Code/JavaAPI/javax.persistence/JoinTableinverseJoinColumns.htm */
    person.setFamily(this);
    persons.add(person);
  }
} 
