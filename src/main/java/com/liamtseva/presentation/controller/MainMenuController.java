package com.liamtseva.presentation.controller;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainMenuController {

  @FXML
  private Button btn_category;

  @FXML
  private Button btn_myGoals;

  @FXML
  private Button btn_steps;

  @FXML
  private Button btn_tips;

  @FXML
  private Button btn_completeGoal;

  @FXML
  private Button btn_exit;

  @FXML
  private StackPane stackPane;

  @FXML
  private StackPane contentArea;

  @FXML
  void initialize() {
    loadMyGoals();
    btn_myGoals.setOnAction(event -> showMyGoalPage());
    btn_completeGoal.setOnAction(event -> showCompleteGoalsPage());
    btn_category.setOnAction(actionEvent -> showCategoryPage());
    btn_steps.setOnAction(event -> showStepsToGoalPage());
    btn_tips.setOnAction(event -> showTipsPage());
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

  private void showMyGoalPage() {
    moveStackPane(btn_myGoals);
    loadFXML("/view/myGoals.fxml");
  }

  private void showCompleteGoalsPage() {
    moveStackPane(btn_completeGoal);
    loadFXML("/view/completeGoals.fxml");
  }
  private void showCategoryPage() {
    moveStackPane(btn_category);
    loadFXML("/view/category.fxml");
  }

  private void showStepsToGoalPage() {
    moveStackPane(btn_steps);
    loadFXML("/view/stepsToGoal.fxml");
  }
  private void showTipsPage() {
    moveStackPane(btn_tips);
    loadFXML("/view/tips.fxml");
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
  private void loadMyGoals() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/myGoals.fxml"));
      AnchorPane myGoalsPane = loader.load();
      // Отримати контролер myGoals.fxml
      MyGoalsController myGoalsController = loader.getController();
      // Вставити myGoalsPane в contentArea
      contentArea.getChildren().clear();
      contentArea.getChildren().add(myGoalsPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}