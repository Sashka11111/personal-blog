package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.entity.Post;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.UserRepository;
import com.liamtseva.persistence.repository.impl.UserRepositoryImpl;
import com.liamtseva.persistence.connection.DatabaseConnection;
import java.io.ByteArrayInputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import javafx.scene.text.Font;

public class SearchController {

  @FXML
  private TextField searchField;

  @FXML
  private VBox usersContainer;

  private UserRepository userRepository;

  public SearchController() {
    this.userRepository = new UserRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  @FXML
  private void initialize() {
    searchField.textProperty().addListener((observable, oldValue, newValue) -> searchUsers(newValue));
    displayAllUsers();
  }

  private void displayAllUsers() {
    List<User> users = userRepository.getAllUsers();
    displayUsers(users);
  }

  private void searchUsers(String searchText) {
    if (searchText.isEmpty()) {
      displayAllUsers();
    } else {
      List<User> foundUsers = userRepository.findByUsernameLike(searchText);
      if (foundUsers.isEmpty()) {
        usersContainer.getChildren().clear();
        Label noUserLabel = new Label("Користувача з ім'ям, що починається з \"" + searchText + "\", не знайдено.");
        noUserLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        usersContainer.getChildren().add(noUserLabel);
      } else {
        displayUsers(foundUsers);
      }
    }
  }

  private void displayUsers(List<User> users) {
    usersContainer.getChildren().clear();
    for (User user : users) {
      HBox userBox = new HBox();
      userBox.setSpacing(10);
      userBox.setStyle("-fx-background-color:  #F5F5DC; -fx-padding: 10; -fx-border-color:  #808080; -fx-border-width: 1;");

      // Load profile image
      ImageView profileImageView = new ImageView();
      byte[] profileImageData = user.profileImage();
      if (profileImageData != null) {
        Image profileImage = new Image(new ByteArrayInputStream(profileImageData));
        profileImageView.setImage(profileImage);
      }
      profileImageView.setFitWidth(50);
      profileImageView.setFitHeight(50);
      profileImageView.setPreserveRatio(true);

      VBox userDetailsBox = new VBox();
      userDetailsBox.setSpacing(5);

      Label usernameLabel = new Label(user.username());
      usernameLabel.setFont(new Font("Arial", 16));
      usernameLabel.setStyle("-fx-font-weight: bold;");

      // Отримати список дописів для поточного користувача
      List<Post> userPosts = userRepository.getUserPosts(user.id());

      // Підрахувати кількість дописів
      int postCount = userPosts.size();

      // Відобразити кількість дописів у Label
      Label postCountLabel = new Label("Кількість дописів: " + postCount);

      // Додати кількість дописів до userDetailsBox
      userDetailsBox.getChildren().addAll(usernameLabel, postCountLabel);


      userBox.getChildren().addAll(profileImageView, userDetailsBox);
      usersContainer.getChildren().add(userBox);
    }
  }
}
