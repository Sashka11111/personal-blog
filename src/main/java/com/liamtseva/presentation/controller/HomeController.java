package com.liamtseva.presentation.controller;

import com.liamtseva.persistence.AuthenticatedUser;
import com.liamtseva.persistence.entity.Post;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.PostRepository;
import com.liamtseva.persistence.repository.impl.PostRepositoryImpl;
import com.liamtseva.persistence.connection.DatabaseConnection;
import java.io.ByteArrayInputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class HomeController {

  @FXML
  private VBox postsContainer;
  @FXML
  private Label welcomeUser;

  private PostRepository postRepository;

  public HomeController() {
    this.postRepository = new PostRepositoryImpl(new DatabaseConnection().getDataSource());
  }

  @FXML
  private void initialize() {
    User currentUser = AuthenticatedUser.getInstance().getCurrentUser();
    welcomeUser.setText("Привіт, "+ currentUser.username());
    displayPosts();
  }

  private void displayPosts() {
    List<Post> posts = postRepository.getAllPosts();
    for (Post post : posts) {
      VBox postBox = new VBox();
      postBox.setStyle("-fx-background-color: #F5F5DC; -fx-padding: 10; -fx-border-color:  #808080; -fx-border-width: 1;");
      postBox.setSpacing(5);

      Label titleLabel = new Label(post.title());
      titleLabel.setFont(new Font("Arial", 16));
      titleLabel.setStyle("-fx-font-weight: bold;");
      Label contentLabel = new Label(post.context());
      contentLabel.setWrapText(true);
      // Перевірка, чи є у поста зображення
      byte[] imageData = post.postImage();
      if (imageData != null && imageData.length > 0) {
        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(imageData)));
        imageView.setFitWidth(150); // Підігнати розмір зображення за замовчуванням
        imageView.setPreserveRatio(true);
        postBox.getChildren().add(imageView);
      }

      postBox.getChildren().addAll(titleLabel, new Separator(), contentLabel);
      postsContainer.getChildren().add(postBox);
    }
  }
}
