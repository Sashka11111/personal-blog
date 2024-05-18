package com.liamtseva.persistence.repository.impl;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Step;
import com.liamtseva.persistence.repository.contract.StepRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class StepRepositoryImpl implements StepRepository {
  private final DataSource dataSource;

  public StepRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void addStep(Step step) throws EntityNotFoundException {
    String query = "INSERT INTO Steps (id_goal, goal_name, description) VALUES (?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      preparedStatement.setInt(1, step.goalId());
      preparedStatement.setString(2, step.goalName());
      preparedStatement.setString(3, step.description());
      preparedStatement.executeUpdate();
      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        step.id();
      } else {
        throw new SQLException("Adding category failed, no ID obtained.");
      }
    } catch (SQLException e) {
      throw new EntityNotFoundException("Failed to add category.", e);
    }
  }


  @Override
  public Step getStepById(int id) throws EntityNotFoundException {
    String query = "SELECT * FROM Steps WHERE id_step = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return extractStepsFromResultSet(resultSet);
        } else {
          throw new EntityNotFoundException("Step with id " + id + " not found");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new EntityNotFoundException("Error while fetching step with id " + id);
    }
  }
  @Override
  public List<Step> getStepsByGoalId(int goalId) {
    List<Step> steps = new ArrayList<>();
    String query = "SELECT * FROM Steps WHERE id_goal = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, goalId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Step step = extractStepsFromResultSet(resultSet);
          steps.add(step);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle exception
    }
    return steps;
  }

  @Override
  public List<Step> getAllSteps() {
    List<Step> steps = new ArrayList<>();
    String query = "SELECT * FROM Steps";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        Step step = extractStepsFromResultSet(resultSet);
        steps.add(step);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle exception
    }
    return steps;
  }

  @Override
  public void updateStep(Step step) throws EntityNotFoundException {
    String query = "UPDATE Steps SET description = ? WHERE id_step = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, step.description());
      preparedStatement.setInt(2, step.id());
      int updatedRows = preparedStatement.executeUpdate();
      if (updatedRows == 0) {
        throw new EntityNotFoundException("Step with id " + step.id() + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle exception
    }
  }

  @Override
  public void deleteStep(int id) throws EntityNotFoundException {
    String query = "DELETE FROM Steps WHERE id_step = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, id);
      int deletedRows = preparedStatement.executeUpdate();
      if (deletedRows == 0) {
        throw new EntityNotFoundException("Step with id " + id + " not found");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle exception
    }
  }

  private Step extractStepsFromResultSet(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id_step");
    int goalId = resultSet.getInt("id_goal");
    String goalName = resultSet.getString("goal_name");
    String description = resultSet.getString("description");
    return new Step(id, goalId, goalName, description);
  }
}
