package com.liamtseva.presentation.viewmodel;

import com.liamtseva.persistence.entity.Step;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class StepViewModel {
  private final SimpleIntegerProperty idStep;
  private final SimpleIntegerProperty idGoal;
  private final SimpleStringProperty description;
  private final SimpleStringProperty goalName;

  public StepViewModel(Step step) {
    this.idStep = new SimpleIntegerProperty(step.id());
    this.idGoal = new SimpleIntegerProperty(step.goalId());
    this.description = new SimpleStringProperty(step.description());
    this.goalName = new SimpleStringProperty(step.goalName());
  }

  public int getIdStep() {
    return idStep.get();
  }

  public SimpleIntegerProperty idStepProperty() {
    return idStep;
  }

  public void setIdStep(int idStep) {
    this.idStep.set(idStep);
  }

  public int getIdGoal() {
    return idGoal.get();
  }

  public SimpleIntegerProperty idGoalProperty() {
    return idGoal;
  }

  public void setIdGoal(int idGoal) {
    this.idGoal.set(idGoal);
  }

  public String getDescription() {
    return description.get();
  }

  public SimpleStringProperty descriptionProperty() {
    return description;
  }

  public void setDescription(String description) {
    this.description.set(description);
  }

  public String getGoalName() {
    return goalName.get();
  }

  public SimpleStringProperty goalNameProperty() {
    return goalName;
  }

  public void setGoalName(String goalName) {
    this.goalName.set(goalName);
  }

  // Метод для отримання об'єкта Step з вмісту ViewModel
  public Step getStep() {
    return new Step(getIdStep(), getIdGoal(), getGoalName(), getDescription());
  }
}
