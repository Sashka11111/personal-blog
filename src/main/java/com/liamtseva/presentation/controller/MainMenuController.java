package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.AuthenticatedUser;
import com.liamtseva.persistence.entity.User;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainMenuController {
  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button btn_addPost;

  @FXML
  private Button btn_exit;

  @FXML
  private Button btn_home;

  @FXML
  private Button btn_myProfile;

  @FXML
  private Button btn_search;

  @FXML
  private StackPane contentArea;

  @FXML
  private StackPane stackPane;

  @FXML
  private ImageView userImageView;

  @FXML
  private Label userName;

  @FXML
  void initialize() {
    UserProfile();
    loadHomePanel();
    btn_home.setOnAction(event -> showHomePage());
    btn_addPost.setOnAction(event -> showAddPostPage());
    btn_search.setOnAction(actionEvent -> showSearchPage());
    btn_myProfile.setOnAction(event -> showMyProfilePage());
    btn_exit.setOnAction(event ->{
      System.exit(0);
    });
  }

  private void moveStackPane(Button button) {
    double buttonX = button.localToParent(button.getBoundsInLocal()).getMinX();
    double buttonY = button.localToParent(button.getBoundsInLocal()).getMinY();
    TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), stackPane);
    transition.setToX(buttonX);
    stackPane.setLayoutY(buttonY);
  }

  private void showHomePage() {
    moveStackPane(btn_home);
    loadFXML("/view/home.fxml");
  }

  private void showAddPostPage() {
    moveStackPane(btn_addPost);
    loadFXML("/view/addPost.fxml");
  }
  private void showSearchPage() {
    moveStackPane(btn_search);
    loadFXML("/view/search.fxml");
  }

  private void showMyProfilePage() {
    moveStackPane(btn_myProfile);
    loadFXML("/view/myProfile.fxml");
  }
  private void loadFXML(String fxmlFileName) {
    try {
      Parent fxml = FXMLLoader.load(getClass().getResource(fxmlFileName));
      contentArea.getChildren().clear(); // Очищаємо contentArea
      contentArea.getChildren().add(fxml); // Додаємо завантажений контент
    } catch (IOException ex) {
      Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  private void loadHomePanel() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
      AnchorPane myGoalsPane = loader.load();
      // Отримати контролер myGoals.fxml
      HomeController homeController = loader.getController();
      // Вставити myGoalsPane в contentArea
      contentArea.getChildren().clear();
      contentArea.getChildren().add(myGoalsPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private void UserProfile() {
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    if (currentUser != null) {
      userName.setText(currentUser.username());

      // Отримання зображення поточного користувача
      byte[] imageBytes = AuthenticatedUser.getInstance().getCurrentUserImage();
      if (imageBytes != null) {
        // Створення об'єкта Image з байтового масиву
        Image profileImage = new Image(new ByteArrayInputStream(imageBytes));
        // Встановлення зображення у profileImageView
        userImageView.setImage(profileImage);
      } else {
       // profileLabel.setText("Користувач не має зображення профілю");
      }
    }
  }
}