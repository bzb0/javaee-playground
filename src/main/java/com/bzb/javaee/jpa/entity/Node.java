package com.bzb.javaee.jpa.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A JPA entity, that represents a node in a tree structure.
 */
@Entity
@Table
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "children", "parent"})
@ToString(exclude = {"creator", "children"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Node {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "nodeIdGen")
  @SequenceGenerator(name = "nodeIdGen", sequenceName = "NODE_SEQ")
  private Long id;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private final Set<Node> children = new HashSet<>();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
  private Node parent;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Lob
  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "CREATION_DATE")
  private LocalDate creationDate = LocalDate.now();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
  private Producer creator;

  /**
   * Adds a new node to the list of children.
   *
   * @param child The child node.
   */
  public void addChild(Node child) {
    Objects.requireNonNull(child, "Child must not be null.");

    /* If the child node already has a parent, we delete the old parent. */
    Optional.ofNullable(child.getParent()).ifPresent(parent -> parent.getChildren().remove(child));

    /* http://stackoverflow.com/questions/2521659/foreign-key-not-stored-in-child-entity-one-to-many
     * www.java2s.com/Code/JavaAPI/javax.persistence/JoinTableinverseJoinColumns.htm */
    child.setParent(this);
    children.add(child);
  }
}

