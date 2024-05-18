package com.liamtseva.persistence.entity;

public record Comment(
    int id,
    int goalId,
    String goalName,
    String description)
    implements Entity, Comparable<Step> {

  @Override
  public int compareTo(Step o) {
    // Порівняння за id
    return Integer.compare(this.id, o.id());
  }
}
