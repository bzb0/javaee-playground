package com.bzb.javaee.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * JPA entity that annotates a {@link Producer}.
 */
@Entity
@Table
@TableGenerator(name = "TagIdGen", table = "ID_GEN", pkColumnValue = "TAG_ID", allocationSize = 1)
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id"})
@ToString(exclude = {"id"})
public class Tag {

  @Id
  @NotNull
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "TagIdGen")
  private long id;

  @NotNull
  @Column(name = "NAME")
  private String name;

}
