package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.AuthenticatedUser;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.UserRepository;
import com.liamtseva.persistence.repository.impl.UserRepositoryImpl;
import com.liamtseva.persistence.connection.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyProfileController {

  @FXML
  private TextField usernameField;

  @FXML
  private ImageView profileImageView;

  @FXML
  private Button saveButton;

  private UserRepository userRepository;
  private User currentUser;

  public MyProfileController() {
    this.userRepository = new UserRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  @FXML
  private void initialize() {
    loadUserProfile();
    saveButton.setOnAction(event -> saveUserProfile());
  }

  private void loadUserProfile() {
    try {
      User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
      usernameField.setText(currentUser.username());

      if (currentUser.profileImage() != null) {
        Image profileImage = new Image(new ByteArrayInputStream(currentUser.profileImage()));
        profileImageView.setImage(profileImage);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void saveUserProfile() {
    try {
      User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
      currentUser = new User(currentUser.id(), usernameField.getText(), currentUser.password(), currentUser.profileImage());
      userRepository.updateUser(currentUser);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
