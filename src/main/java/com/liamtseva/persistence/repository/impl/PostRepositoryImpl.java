package com.liamtseva.persistence.repository.impl;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Post;
import com.liamtseva.persistence.repository.contract.GoalRepository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class GoalRepositoryImpl implements GoalRepository {
  private DataSource dataSource;

  public GoalRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void addGoal(Post goal) {
    String query = "INSERT INTO Goals (id_user, name_goal, description, id_category, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, goal.userId());
      preparedStatement.setString(2, goal.nameGoal());
      preparedStatement.setString(3, goal.description());
      preparedStatement.setInt(4, goal.categoryId());
      preparedStatement.setDate(5, Date.valueOf(goal.startDate()));
      preparedStatement.setDate(6, Date.valueOf(goal.endDate()));
      preparedStatement.setString(7, goal.status());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Post getGoalById(int id) throws EntityNotFoundException {
    String query = "SELECT * FROM Goals WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return extractGoalFromResultSet(resultSet);
      } else {
        throw new EntityNotFoundException("Post with id " + id + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new EntityNotFoundException("Error while fetching goal with id " + id);
    }
  }

  @Override
  public List<Post> getAllGoals() {
    List<Post> goals = new ArrayList<>();
    String query = "SELECT * FROM Goals"; // Запит для вибору цілей, які належать поточному користувачеві
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      //int currentUserId = getCurrentUserId(); // Отримання ідентифікатора поточного користувача
     // preparedStatement.setInt(1, currentUserId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Post goal = extractGoalFromResultSet(resultSet);
          goals.add(goal);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return goals;
  }
  @Override
  public Post getGoalByName(String name) throws EntityNotFoundException {
    String query = "SELECT * FROM Goals WHERE name_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, name);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return extractGoalFromResultSet(resultSet);
      } else {
        throw new EntityNotFoundException("Post with name " + name + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new EntityNotFoundException("Error while fetching goal with name " + name);
    }
  }
  @Override
  public void updateGoalStatusByName(String goalName, String newStatus) throws EntityNotFoundException {
    String query = "UPDATE Goals SET status = ? WHERE name_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, newStatus);
      statement.setString(2, goalName);
      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated == 0) {
        throw new EntityNotFoundException("Post with name " + goalName + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // Обробка помилок бази даних
    }
  }

  @Override
  public List<Post> filterGoalsByUserId(int userId) {
    List<Post> goals = new ArrayList<>();
    String query = "SELECT * FROM Goals WHERE id_user = ?"; // Filter by user ID
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, userId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Post goal = extractGoalFromResultSet(resultSet);
          goals.add(goal);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // Implement error handling
    }
    return goals;
  }
  @Override
  public List<Post> getAllGoalsByUserId(int userId) {
    List<Post> goals = new ArrayList<>();
    String query = "SELECT * FROM Goals WHERE id_user = ?"; // Filter by user ID
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, userId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Post goal = extractGoalFromResultSet(resultSet);
          goals.add(goal);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return goals;
  }
  @Override
  public void updateGoal(Post goal) throws EntityNotFoundException {
    String query = "UPDATE Goals SET id_user = ?, name_goal = ?, description = ?, id_category = ?, start_date = ?, end_date = ?, status = ? WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, goal.userId());
      preparedStatement.setString(2, goal.nameGoal());
      preparedStatement.setString(3, goal.description());
      preparedStatement.setInt(4, goal.categoryId());
      preparedStatement.setDate(5, Date.valueOf(goal.startDate()));
      preparedStatement.setDate(6, Date.valueOf(goal.endDate()));
      preparedStatement.setString(7, goal.status());
      preparedStatement.setInt(8, goal.id());
      int rowsUpdated = preparedStatement.executeUpdate();
      if (rowsUpdated == 0) {
        throw new EntityNotFoundException("Post with id " + goal.id() + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new EntityNotFoundException("Error while updating goal with id " + goal.id());
    }
  }
  @Override
  public void updateGoalStatus(int goalId, String newStatus) throws EntityNotFoundException {
    String query = "UPDATE Goals SET status = ? WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, newStatus);
      statement.setInt(2, goalId);
      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated == 0) {
        throw new EntityNotFoundException("Post with id " + goalId + " not found");
      }
    } catch (SQLException e) {
      // Обробка помилок бази даних
      e.printStackTrace();
    }
  }
  @Override
  public void deleteGoal(int id) throws EntityNotFoundException {
    String query = "DELETE FROM Goals WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      int rowsDeleted = preparedStatement.executeUpdate();
      if (rowsDeleted == 0) {
        throw new EntityNotFoundException("Post with id " + id + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new EntityNotFoundException("Error while deleting goal with id " + id);
    }
  }

  private Post extractGoalFromResultSet(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id_goal");
    int userId = resultSet.getInt("id_user");
    String nameGoal = resultSet.getString("name_goal");
    String description = resultSet.getString("description");
    int categoryId = resultSet.getInt("id_category");
    LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
    LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
    String status = resultSet.getString("status");
    return new Post(id, userId, nameGoal, description, categoryId, startDate, endDate, status);
  }
}