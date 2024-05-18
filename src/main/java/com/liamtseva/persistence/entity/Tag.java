package com.liamtseva.persistence.entity;

public record Tips(
    int id,
    String tipsText)
    implements Entity, Comparable<Tips> {

  @Override
  public int compareTo(Tips o) {
    // Порівняння за id
    return Integer.compare(this.id, o.id());
  }
}
