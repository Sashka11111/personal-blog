package com.liamtseva.presentation.controller;

import com.liamtseva.domain.validation.UserValidator;
import com.liamtseva.persistence.connection.DatabaseConnection;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.UserRepository;
import com.liamtseva.persistence.repository.impl.UserRepositoryImpl;
import com.liamtseva.presentation.animation.Shake;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class RegistrationController {

  @FXML
  private Button SignInButton;

  @FXML
  private Button authSignInButton;

  @FXML
  private TextField login_field;

  @FXML
  private PasswordField password_field;

  @FXML
  private Label errorMessageLabel;

  @FXML
  private Label errorPassword;

  @FXML
  private ImageView profileImageView;

  private String selectedProfileImagePath;

  private UserRepository userRepository;
  private byte[] imageBytes;

  public RegistrationController() {
    this.userRepository = new UserRepositoryImpl(new DatabaseConnection().getDataSource());
    // Завантажити зображення за замовчуванням
    imageBytes = loadDefaultImageBytes();
  }

  private byte[] readImageToBytes(File file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      byte[] data = new byte[(int) file.length()];
      fis.read(data);
      return data;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private byte[] loadDefaultImageBytes() {
    try (InputStream is = getClass().getResourceAsStream("/data/profile.png")) {
      if (is == null) {
        throw new IOException("Default profile image not found");
      }
      return is.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @FXML
  void chooseImageButtonClicked() {
    chooseImage();
  }

  public void chooseImage() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose Profile Image");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
    );
    File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());
    if (selectedFile != null) {
      selectedProfileImagePath = selectedFile.getPath();
      Image image = new Image(selectedFile.toURI().toString());
      profileImageView.setImage(image);
      imageBytes = readImageToBytes(selectedFile);
    } else {
      profileImageView.setImage(new Image(getClass().getResourceAsStream("/data/profile.png")));
      imageBytes = loadDefaultImageBytes();
    }
  }

  @FXML
  void initialize() {
    authSignInButton.setOnAction(event -> {
      Scene currentScene = authSignInButton.getScene();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/authorization.fxml"));
      try {
        Parent root = loader.load();
        currentScene.setRoot(root);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    SignInButton.setOnAction(event -> {
      String username = login_field.getText();
      String password = password_field.getText();
      if (username.isEmpty() || password.isEmpty()) {
        errorMessageLabel.setText("Логін та пароль не повинні бути порожніми");
        Shake userLoginAnim = new Shake(login_field);
        Shake userPassAnim = new Shake(password_field);
        userLoginAnim.playAnim();
        userPassAnim.playAnim();
        return;
      }
      if (UserValidator.isUsernameValid(username) && UserValidator.isPasswordValid(password)) {
        if (!userRepository.isUsernameExists(username)) {
          User user = new User(0, username, password, imageBytes);
          userRepository.addUser(user);

          Scene currentScene = authSignInButton.getScene();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/authorization.fxml"));
          try {
            Parent root = loader.load();
            currentScene.setRoot(root);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        } else {
          errorMessageLabel.setText("Логін з ім'ям " + username + " уже існує");
          Shake userLoginAnim = new Shake(login_field);
          userLoginAnim.playAnim();
        }
      } else {
        errorMessageLabel.setText("Пароль має містити велику, маленьку букву та цифру.");
        errorPassword.setText("Мінімальна довжина паролю: 6 символів");
        Shake userLoginAnim = new Shake(login_field);
        Shake userPassAnim = new Shake(password_field);
        userLoginAnim.playAnim();
        userPassAnim.playAnim();
      }
    });
  }
}
