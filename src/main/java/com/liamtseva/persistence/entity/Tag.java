package com.liamtseva.persistence.entity;

public record Tag(
    int id,
    String name)
    implements Entity, Comparable<Tag> {

  @Override
  public int compareTo(Tag o) {
    // Порівняння за id
    return Integer.compare(this.id, o.id());
  }
}
