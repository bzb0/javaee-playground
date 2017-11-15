package com.bzb.javaee.jpa.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.drus.jpa.unit.api.TransactionMode;
import eu.drus.jpa.unit.api.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

@Transactional(TransactionMode.DISABLED)
public class NodeIT extends EntityIT<Node> {

  private static final Class<Node> ENTITY_CLASS = Node.class;

  @Test
  public void whenPersistNode_thenAllPropertiesSet() {
    // given
    Node node = new Node();
    node.setName("Root");
    node.setCreationDate(LocalDate.now());
    node.setDescription("Root Node");

    // when
    persist(node);

    // then
    assertNotNull(node.getId());
    Node dbEntity = find(node.getId(), ENTITY_CLASS).orElse(null);

    assertEquals(node.getName(), dbEntity.getName());
    assertEquals(node.getDescription(), dbEntity.getDescription());
    assertEquals(node.getCreationDate(), dbEntity.getCreationDate());
    assertNull(dbEntity.getParent());
  }

  @Test
  public void whenPersistTree_thenCompleteTreeIsStored() {
    // given
    Node root = Node.builder().name("root").build();
    Node childA = Node.builder().name("A").build();
    Node childB = Node.builder().name("B").build();
    Node childC = Node.builder().name("C").build();
    root.addChild(childA);
    root.addChild(childB);
    childA.addChild(childC);

    // when
    persist(root);

    // then
    assertNotNull(root.getId());
    assertNotNull(childB.getId());
    assertNotNull(childC.getId());

    Node dbChildC = find(childC.getId(), ENTITY_CLASS).orElseThrow(() -> new NullPointerException("The node 'C' was not stored in the DB."));
    assertEquals(childA, dbChildC.getParent());
    assertEquals(root, dbChildC.getParent().getParent());
  }

  @Test
  public void whenDeleteTree_thenFindReturnsNull() {
    // given
    Node root = Node.builder().name("root").build();
    Node childA = Node.builder().name("A").build();
    Node childB = Node.builder().name("B").build();
    Node childC = Node.builder().name("C").build();
    root.addChild(childA);
    root.addChild(childB);
    childA.addChild(childC);

    // when
    persist(root);
    delete(childC.getId(), ENTITY_CLASS);

    // then
    assertFalse(find(childC.getId(), ENTITY_CLASS).isPresent());
    assertTrue(find(childA.getId(), ENTITY_CLASS).isPresent());

    // when
    delete(root.getId(), ENTITY_CLASS);

    // then
    assertFalse(find(root.getId(), ENTITY_CLASS).isPresent());
    assertFalse(find(childA.getId(), ENTITY_CLASS).isPresent());
    assertFalse(find(childB.getId(), ENTITY_CLASS).isPresent());
    assertFalse(find(childC.getId(), ENTITY_CLASS).isPresent());
  }

  @Test
  public void whenMultiLevelTreeDelete_() {
    // given
    Node root = Node.builder().name("root").build();

    List<Node> children = IntStream.range(1, 100).mapToObj(i -> {
      Node node = new Node();
      node.setName("level" + i);
      return node;
    }).collect(Collectors.toList());

    root.addChild(children.get(0));
    for (int i = 0; i < children.size() - 1; i++) {
      Node parent = children.get(i);
      Node child = children.get(i + 1);
      parent.addChild(child);
    }

    // when
    persist(root);
    // then
    assertNotNull(root.getId());
    children.forEach(child -> assertNotNull(child.getId()));

    // when
    delete(root.getId(), ENTITY_CLASS);
    // then
    assertFalse(find(root.getId(), ENTITY_CLASS).isPresent());
    children.forEach(child -> assertFalse(find(child.getId(), ENTITY_CLASS).isPresent()));
  }
}