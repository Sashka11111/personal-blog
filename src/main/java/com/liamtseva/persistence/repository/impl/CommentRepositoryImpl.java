package com.liamtseva.persistence.repository.impl;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Comment;
import com.liamtseva.persistence.repository.contract.CommentRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class CommentRepositoryImpl implements CommentRepository {
  private final DataSource dataSource;

  public CommentRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void addComment(Comment comment) throws EntityNotFoundException {
    String sql = "INSERT INTO Comment (post_id, user_id, content, created_at) VALUES (?, ?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, comment.postId());
      statement.setInt(2, comment.userId());
      statement.setString(3, comment.content());
      statement.setDate(4, Date.valueOf(comment.createdDate()));
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error adding comment", e);
    }
  }

  @Override
  public Comment getCommentById(int id) throws EntityNotFoundException {
    String sql = "SELECT * FROM Comment WHERE comment_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return extractCommentFromResultSet(resultSet);
        } else {
          throw new EntityNotFoundException("Comment not found with id " + id);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching comment by id", e);
    }
  }

  @Override
  public List<Comment> getCommentsByPostId(int postId) {
    String sql = "SELECT * FROM Comment WHERE post_id = ?";
    List<Comment> comments = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, postId);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          comments.add(extractCommentFromResultSet(resultSet));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching comments by post id", e);
    }
    return comments;
  }

  @Override
  public List<Comment> getAllComments() {
    String sql = "SELECT * FROM Comment";
    List<Comment> comments = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        comments.add(extractCommentFromResultSet(resultSet));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching all comments", e);
    }
    return comments;
  }

  @Override
  public void updateComment(Comment comment) throws EntityNotFoundException {
    String sql = "UPDATE Comment SET post_id = ?, user_id = ?, content = ?, created_at = ? WHERE comment_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, comment.postId());
      statement.setInt(2, comment.userId());
      statement.setString(3, comment.content());
      statement.setDate(4, Date.valueOf(comment.createdDate()));
      statement.setInt(5, comment.id());

      int rowsAffected = statement.executeUpdate();
      if (rowsAffected == 0) {
        throw new EntityNotFoundException("Comment not found with id " + comment.id());
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error updating comment", e);
    }
  }

  @Override
  public void deleteComment(int id) throws EntityNotFoundException {
    String sql = "DELETE FROM Comment WHERE comment_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);

      int rowsAffected = statement.executeUpdate();
      if (rowsAffected == 0) {
        throw new EntityNotFoundException("Comment not found with id " + id);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error deleting comment", e);
    }
  }

  private Comment extractCommentFromResultSet(ResultSet resultSet) throws SQLException {
    int commentId = resultSet.getInt("comment_id");
    int postId = resultSet.getInt("post_id");
    int userId = resultSet.getInt("user_id");
    String content = resultSet.getString("content");
    LocalDate createdAt = resultSet.getDate("created_at").toLocalDate();

    return new Comment(commentId, postId, userId, content, createdAt);
  }
}
