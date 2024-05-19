package com.liamtseva.presentation.viewmodel;

import javafx.beans.property.*;

import java.time.LocalDate;

public class PostViewModel {
  private final IntegerProperty id;
  private final IntegerProperty userId;
  private final StringProperty title;
  private final StringProperty content;
  private final ObjectProperty<LocalDate> createdDate;

  public PostViewModel(int id, int userId, String title, String content, LocalDate createdDate) {
    this.id = new SimpleIntegerProperty(id);
    this.userId = new SimpleIntegerProperty(userId);
    this.title = new SimpleStringProperty(title);
    this.content = new SimpleStringProperty(content);
    this.createdDate = new SimpleObjectProperty<>(createdDate);
  }

  public PostViewModel() {
    this(0, 0, "", "", LocalDate.now());
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

  public int getUserId() {
    return userId.get();
  }

  public IntegerProperty userIdProperty() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId.set(userId);
  }

  public String getTitle() {
    return title.get();
  }

  public StringProperty titleProperty() {
    return title;
  }

  public void setTitle(String title) {
    this.title.set(title);
  }

  public String getContent() {
    return content.get();
  }

  public StringProperty contentProperty() {
    return content;
  }

  public void setContent(String content) {
    this.content.set(content);
  }

  public LocalDate getCreatedDate() {
    return createdDate.get();
  }

  public ObjectProperty<LocalDate> createdDateProperty() {
    return createdDate;
  }

  public void setCreatedDate(LocalDate createdDate) {
    this.createdDate.set(createdDate);
  }

  @Override
  public String toString() {
    return "PostViewModel{" +
        "id=" + id.get() +
        ", userId=" + userId.get() +
        ", title='" + title.get() + '\'' +
        ", content='" + content.get() + '\'' +
        ", createdDate=" + createdDate.get() +
        '}';
  }
}

