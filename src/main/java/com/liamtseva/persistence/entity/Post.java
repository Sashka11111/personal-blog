package com.liamtseva.persistence.entity;

import java.time.LocalDate;

public record Post(
    int id,
    int userId,
    String nameGoal,
    String description,
    int categoryId,
    LocalDate startDate,
    LocalDate endDate,
    String status)
    implements Entity, Comparable<Goal> {

  @Override
  public int compareTo(Goal o) {
    return Integer.compare(this.id, o.id());
  }
}
