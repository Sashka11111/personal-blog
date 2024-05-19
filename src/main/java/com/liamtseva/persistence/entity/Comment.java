package com.liamtseva.persistence.entity;

import java.time.LocalDate;

public record Comment(
    int id,
    int postId,
    int userId,
    String content,
    LocalDate createdDate)
    implements Entity, Comparable<Comment> {

  @Override
  public int compareTo(Comment o) {
    // Порівняння за id
    return Integer.compare(this.id, o.id());
  }
}
