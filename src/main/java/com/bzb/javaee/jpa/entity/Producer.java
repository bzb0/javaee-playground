package com.bzb.javaee.jpa.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A JPA entity, that represents a {@link Node} producer. A {@link Producer} can be annotated with different tags.
 */
@Entity
@Table
@NamedQueries({
    @NamedQuery(name = "Producer.findAll", query = "SELECT p FROM Producer p"),
    @NamedQuery(name = "Producer.findByName", query = "SELECT p FROM Producer p WHERE p.name = :name"),
    @NamedQuery(name = "Producer.findFastProducers", query = "SELECT p FROM Producer p INNER JOIN p.tags tag WHERE tag.name = 'fast'")
})
@TableGenerator(name = "ProducerIdGen", table = "ID_GEN", pkColumnValue = "PRODUCER_ID", allocationSize = 1)
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id"})
@ToString(exclude = {"id"})
public class Producer {

  @Id
  @NotNull
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ProducerIdGen")
  private long id;

  @NotNull
  @Column(name = "NAME")
  private String name;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable//(joinColumns = @JoinColumn(name = "PRODUCER_ID"), inverseJoinColumns = @JoinColumn(name = "TAG_ID"))
  private List<Tag> tags;

}
