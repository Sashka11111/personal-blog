package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.UserRepository;
import com.liamtseva.persistence.repository.impl.UserRepositoryImpl;
import com.liamtseva.persistence.connection.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
      Label userLabel = new Label();
      userLabel.setText("Username: " + user.username());
      userLabel.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #d0d0d0; -fx-border-width: 1;");
      usersContainer.getChildren().add(userLabel);
    }
  }
}
