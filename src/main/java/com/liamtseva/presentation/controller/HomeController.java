package com.liamtseva;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class HomeController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button btn_createPost;

  @FXML
  private Button btn_exit;

  @FXML
  private Button btn_home;

  @FXML
  private Button btn_profile;

  @FXML
  private Button btn_search;

  @FXML
  private StackPane stackPane;

  @FXML
  void initialize() {
    btn_home.setOnAction(event -> loadHomePanel());
    // Додати обробники подій для всіх кнопок
    btn_home.setOnAction(event -> moveStackPane(btn_home));
    btn_search.setOnAction(event -> moveStackPane(btn_search));
    btn_createPost.setOnAction(event -> moveStackPane(btn_createPost));
    btn_profile.setOnAction(event -> moveStackPane(btn_profile));
    btn_exit.setOnAction(event -> moveStackPane(btn_exit));

    // Додати ефект підсвічування при наведенні на кнопку
    addHoverEffect(btn_home);
    addHoverEffect(btn_search);
    addHoverEffect(btn_createPost);
    addHoverEffect(btn_profile);
    addHoverEffect(btn_exit);
  }

  private void addHoverEffect(Button button) {
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(5.0);
    dropShadow.setOffsetX(2.0);
    dropShadow.setOffsetY(2.0);

    button.setOnMouseEntered(event -> button.setEffect(dropShadow));
    button.setOnMouseExited(event -> button.setEffect(null));
  }

  private void moveStackPane(Button button) {
    double buttonX = button.localToParent(button.getBoundsInLocal()).getMinX();
    double buttonY = button.localToParent(button.getBoundsInLocal()).getMinY();

    TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), stackPane);
    transition.setToX(buttonX);
    stackPane.setLayoutY(buttonY);
  }
  private void loadHomePanel() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("homePanel.fxml"));
      AnchorPane homePanel = loader.load();
      // Очищаємо поточний вміст кореневого AnchorPane
      stackPane.getChildren().setAll(homePanel);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}