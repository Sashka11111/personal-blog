package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.AuthenticatedUser;
import com.liamtseva.persistence.entity.Category;
import com.liamtseva.persistence.entity.Post;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.CategoryRepository;
import com.liamtseva.persistence.repository.contract.PostRepository;
import com.liamtseva.persistence.repository.impl.CategoryRepositoryImpl;
import com.liamtseva.persistence.repository.impl.PostRepositoryImpl;
import com.liamtseva.persistence.connection.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddPostController {

  @FXML
  private TextField titleField;

  @FXML
  private TextArea contentField;

  @FXML
  private Button addPostButton;

  @FXML
  private Label messageLabel;

  @FXML
  private ComboBox<Category> category;

  private PostRepository postRepository;
  private final CategoryRepository categoryRepository;

  public AddPostController() {
    this.postRepository = new PostRepositoryImpl(new DatabaseConnection().getDataSource());
    this.categoryRepository = new CategoryRepositoryImpl(new DatabaseConnection().getDataSource()); // Створення CategoryRepositoryImpl з DatabaseConnection
  }

  @FXML
  private void initialize() {
    // Ініціалізація ComboBox категорій
    ObservableList<Category> categories = FXCollections.observableArrayList();
    try {
      categories.addAll(categoryRepository.getAllCategories());
      category.setItems(categories);
    } catch (Exception e) {
      messageLabel.setText("Error loading categories.");
      e.printStackTrace();
    }

    addPostButton.setOnAction(event -> addPost());
  }

  private void addPost() {
    Category selectedCategory = category.getValue();
    String title = titleField.getText();
    String content = contentField.getText();
    if (!title.isEmpty() && !content.isEmpty() && selectedCategory != null) {
      User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
      if (currentUser != null) {
        Post post = new Post(0, currentUser.id(), selectedCategory.id(), title, content);
        postRepository.addPost(post);
        messageLabel.setText("Допис успішно додано.");
        clearFields();
      }
    } else {
      messageLabel.setText("Назва, вміст і категорія не можуть бути пустими.");
    }
  }

  private void clearFields() {
    titleField.clear();
    contentField.clear();
    category.getSelectionModel().clearSelection();
  }
}
