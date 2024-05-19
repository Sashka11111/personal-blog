package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.AuthenticatedUser;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.UserRepository;
import com.liamtseva.persistence.repository.impl.UserRepositoryImpl;
import com.liamtseva.persistence.connection.DatabaseConnection;
import java.io.ByteArrayInputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MyProfileController {

  @FXML
  private Label profileLabel;

  @FXML
  private ImageView profileImageView;

  private UserRepository userRepository;

  public MyProfileController() {
    this.userRepository = new UserRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  @FXML
  private void initialize() {
    displayUserProfile();
  }

  private void displayUserProfile() {
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    if (currentUser != null) {
      profileLabel.setText("Мій профіль: " + currentUser.username());

      // Отримання зображення поточного користувача
      byte[] imageBytes = AuthenticatedUser.getInstance().getCurrentUserImage();
      if (imageBytes != null) {
        // Створення об'єкта Image з байтового масиву
        Image profileImage = new Image(new ByteArrayInputStream(imageBytes));
        // Встановлення зображення у profileImageView
        profileImageView.setImage(profileImage);
      } else {
        profileLabel.setText("Користувач не має зображення профілю");
      }
    } else {
      profileLabel.setText("Користувач не аутентифікований");
    }
  }

  private void loadImage(String imagePath) {
    if (imagePath != null && !imagePath.isEmpty()) {
      try {
        Image image = new Image(new FileInputStream(imagePath));
        profileImageView.setImage(image);
      } catch (FileNotFoundException e) {
        profileLabel.setText("Не вдалося завантажити зображення профілю");
      }
    }
  }
}
