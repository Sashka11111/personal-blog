package com.liamtseva.persistence.entity;

import java.time.LocalDate;

public record Post(
    int id,
    int userId,
    int categoryId,
    String title,
    String context,
    byte[] postImage)
    implements Entity, Comparable<Post> {

  @Override
  public int compareTo(Post o) {
    return Integer.compare(this.id, o.id());
  }
}
