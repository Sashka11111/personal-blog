package com.liamtseva.persistence;

import com.liamtseva.persistence.entity.User;

public class AuthenticatedUser {
  private static AuthenticatedUser instance;
  private User currentUser;

  private AuthenticatedUser() {}

  public static AuthenticatedUser getInstance() {
    if (instance == null) {
      instance = new AuthenticatedUser();
    }
    return instance;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User user) {
    this.currentUser = user;
  }

  // Додавання методу для отримання зображення поточного користувача
  public byte[] getCurrentUserImage() {
    if (currentUser != null) {
      return currentUser.profileImage();
    } else {
      return null;
    }
  }
}
