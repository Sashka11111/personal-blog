package com.liamtseva.persistence.repository.impl;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Post;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.UserRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javax.sql.DataSource;

public class UserRepositoryImpl implements UserRepository {

  private DataSource dataSource;

  public UserRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void addUser(User user) {
    String query = "INSERT INTO User (username, password, profile_image) VALUES (?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, user.username());
      statement.setString(2, user.password());
      statement.setBytes(3, user.profileImage());
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public User getUserById(int id) throws EntityNotFoundException {
    String query = "SELECT * FROM User WHERE user_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return mapUser(resultSet);
        } else {
          throw new EntityNotFoundException("User with id " + id + " not found");
        }
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Error while fetching user with id " + id, e);
    }
  }

  @Override
  public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    String query = "SELECT * FROM User";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        users.add(mapUser(resultSet));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  @Override
  public User findByUsername(String username) throws EntityNotFoundException {
    String query = "SELECT * FROM User WHERE username = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, username);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return mapUser(resultSet);
        } else {
          throw new EntityNotFoundException("User with username " + username + " not found");
        }
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Error while fetching user by username: " + username, e);
    }
  }
  public List<User> findByUsernameLike(String username) {
    String query = "SELECT * FROM User WHERE username LIKE ?";
    List<User> users = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, username + "%");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          users.add(mapUser(resultSet));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }
  @Override
  public boolean isUsernameExists(String username) {
    String query = "SELECT COUNT(*) FROM User WHERE username = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, username);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          int count = resultSet.getInt(1);
          return count > 0;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void updateUser(User user) throws EntityNotFoundException {
    String sql = "UPDATE User SET username = ?, password = ?, profile_image = ? WHERE user_id = ?";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, user.username());
      statement.setString(2, user.password());

      if (user.profileImage() != null) {
        statement.setBytes(3, user.profileImage());
      } else {
        statement.setNull(3, java.sql.Types.BLOB);
      }

      statement.setInt(4, user.id());

      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated == 0) {
        throw new EntityNotFoundException("User with id " + user.id() + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void deleteUser(int id) throws EntityNotFoundException {
    String query = "DELETE FROM User WHERE user_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      int rowsDeleted = preparedStatement.executeUpdate();
      if (rowsDeleted == 0) {
        throw new EntityNotFoundException("User with id " + id + " not found");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Error while deleting user with id " + id, e);
    }
  }
  @Override
  public List<Post> getUserPosts(int userId) {
    List<Post> userPosts = new ArrayList<>();
    String query = "SELECT * FROM Post WHERE user_id = ?";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setInt(1, userId);

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          // Отримання даних про допис і додавання їх до списку userPosts
          int postId = resultSet.getInt("post_id");
          int categoryId = resultSet.getInt("category_id");
          String title = resultSet.getString("title");
          String content = resultSet.getString("content");
          byte[] postImage = resultSet.getBytes("post_image");

          Post post = new Post(postId, userId, categoryId, title, content, postImage);

          userPosts.add(post);
        }
      }
    } catch (SQLException e) {
      // Обробка винятку, якщо потрібно
      e.printStackTrace();
    }

    return userPosts;
  }
  private User mapUser(ResultSet resultSet) throws SQLException {
    return new User(
        resultSet.getInt("user_id"),
        resultSet.getString("username"),
        resultSet.getString("password"),
        resultSet.getBytes("profile_image")
    );
  }
}
