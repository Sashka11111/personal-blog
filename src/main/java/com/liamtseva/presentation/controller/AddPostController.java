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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

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
  @FXML
  private ImageView postImageView;

  @FXML
  private Button chooseImageButton; // Додано кнопку вибору зображення

  private PostRepository postRepository;
  private final CategoryRepository categoryRepository;

  private byte[] imageBytes; // Додано змінну для зберігання байтів зображення

  public AddPostController() {
    this.postRepository = new PostRepositoryImpl(new DatabaseConnection().getDataSource());
    this.categoryRepository = new CategoryRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  @FXML
  private void initialize() {
    ObservableList<Category> categories = FXCollections.observableArrayList();
    try {
      categories.addAll(categoryRepository.getAllCategories());
      category.setItems(categories);
    } catch (Exception e) {
      messageLabel.setText("Error loading categories.");
      e.printStackTrace();
    }

    addPostButton.setOnAction(event -> addPost());
    chooseImageButton.setOnAction(event -> chooseImage());
  }

  private void addPost() {
    Category selectedCategory = category.getValue();
    String title = titleField.getText();
    String content = contentField.getText();
    if (!title.isEmpty() && !content.isEmpty() && selectedCategory != null) {
      User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
      if (currentUser != null) {
        Post post = new Post(0, currentUser.id(), selectedCategory.id(), title, content, imageBytes); // Додано передачу imageBytes
        postRepository.addPost(post);
        messageLabel.setText("Допис успішно додано.");
        clearFields();
      }
    } else {
      messageLabel.setText("Назва, вміст і категорія не можуть бути пустими.");
    }
  }

  private void chooseImage() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Виберіть зображення");
    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
      try (FileInputStream fis = new FileInputStream(selectedFile)) {
        imageBytes = new byte[(int) selectedFile.length()];
        fis.read(imageBytes);
        // Оновіть ImageView з вибраним зображенням
        Image image = new Image(selectedFile.toURI().toString());
        postImageView.setImage(image);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void clearFields() {
    titleField.clear();
    contentField.clear();
    category.getSelectionModel().clearSelection();
    imageBytes = null; // Очистка байтів зображення після додавання допису
  }
}
