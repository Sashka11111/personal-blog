package com.liamtseva.persistence.repository.impl;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.persistence.repository.contract.UserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    String query = "UPDATE User SET username = ?, password = ?, profile_image = ? WHERE user_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, user.username());
      preparedStatement.setString(2, user.password());
      preparedStatement.setBytes(3, user.profileImage());
      preparedStatement.setInt(5, user.id());
      int rowsUpdated = preparedStatement.executeUpdate();
      if (rowsUpdated == 0) {
        throw new EntityNotFoundException("User with id " + user.id() + " not found");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Error while updating user with id " + user.id(), e);
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

  private User mapUser(ResultSet resultSet) throws SQLException {
    return new User(
        resultSet.getInt("user_id"),
        resultSet.getString("username"),
        resultSet.getString("password"),
        resultSet.getBytes("profile_image")
    );
  }
}
