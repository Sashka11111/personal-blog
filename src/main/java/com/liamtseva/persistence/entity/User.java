package com.liamtseva.persistence.entity;

public record User(
    int id,
    String username,
    String password,
    byte[] profileImage)
    implements Entity, Comparable<User> {

  @Override
  public int compareTo(User o) {
    return this.username.compareTo(o.username);
  }
}
