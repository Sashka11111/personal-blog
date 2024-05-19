package com.liamtseva.presentation.viewmodel;

import javafx.beans.property.*;

public class TagViewModel {
  private final IntegerProperty id;
  private final StringProperty name;

  public TagViewModel(int id, String name) {
    this.id = new SimpleIntegerProperty(id);
    this.name = new SimpleStringProperty(name);
  }

  public TagViewModel() {
    this(0, "");
  }

  public int getId() {
    return id.get();
  }

  public IntegerProperty idProperty() {
    return id;
  }

  public void setId(int id) {
    this.id.set(id);
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  @Override
  public String toString() {
    return "TagViewModel{" +
        "id=" + id.get() +
        ", name='" + name.get() + '\'' +
        '}';
  }
}
