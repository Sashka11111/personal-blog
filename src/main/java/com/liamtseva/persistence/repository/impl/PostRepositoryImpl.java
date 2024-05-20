package com.liamtseva.persistence.repository.impl;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Post;
import com.liamtseva.persistence.repository.contract.PostRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class PostRepositoryImpl implements PostRepository {
  private DataSource dataSource;

  public PostRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void addPost(Post post) {
    String sql = "INSERT INTO Post (user_id, category_id, title, content, post_image) VALUES (?, ?, ?, ?, ?)";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, post.userId());
      statement.setInt(2, post.categoryId());
      statement.setString(3, post.title());
      statement.setString(4, post.context());
      statement.setBytes(5, post.postImage());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error adding post", e);
    }
  }

  @Override
  public Post getPostById(int id) throws EntityNotFoundException {
    String sql = "SELECT * FROM Post WHERE post_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return extractPostFromResultSet(resultSet);
        } else {
          throw new EntityNotFoundException("Post not found with id " + id);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching post by id", e);
    }
  }

  @Override
  public List<Post> getAllPosts() {
    String sql = "SELECT * FROM Post";
    List<Post> posts = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        posts.add(extractPostFromResultSet(resultSet));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching all posts", e);
    }
    return posts;
  }

  @Override
  public Post getPostByName(String name) throws EntityNotFoundException {
    String sql = "SELECT * FROM Post WHERE title = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, name);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return extractPostFromResultSet(resultSet);
        } else {
          throw new EntityNotFoundException("Post not found with title " + name);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching post by name", e);
    }
  }

  @Override
  public List<Post> filterPostsByUserId(int userId) {
    String sql = "SELECT * FROM Post WHERE user_id = ?";
    List<Post> posts = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, userId);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          posts.add(extractPostFromResultSet(resultSet));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error filtering posts by user id", e);
    }
    return posts;
  }

  @Override
  public List<Post> getAllPostsByUserId(int userId) {
    return filterPostsByUserId(userId);
  }

  @Override
  public void updatePost(Post post) throws EntityNotFoundException {
    String sql = "UPDATE Post SET user_id = ?, category_id = ?, title = ?, content = ?, post_image = ? WHERE post_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, post.userId());
      statement.setInt(2, post.categoryId());
      statement.setString(3, post.title());
      statement.setString(4, post.context());
      statement.setBytes(5, post.postImage());
      statement.setInt(6, post.id());

      int rowsAffected = statement.executeUpdate();
      if (rowsAffected == 0) {
        throw new EntityNotFoundException("Post not found with id " + post.id());
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error updating post", e);
    }
  }

  @Override
  public void deletePost(int id) throws EntityNotFoundException {
    String sql = "DELETE FROM Post WHERE post_id = ?";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);

      int rowsAffected = statement.executeUpdate();
      if (rowsAffected == 0) {
        throw new EntityNotFoundException("Post not found with id " + id);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error deleting post", e);
    }
  }

  private Post extractPostFromResultSet(ResultSet resultSet) throws SQLException {
    int postId = resultSet.getInt("post_id");
    int userId = resultSet.getInt("user_id");
    int categoryId = resultSet.getInt("category_id");
    String title = resultSet.getString("title");
    String content = resultSet.getString("content");
    byte[] postImage = resultSet.getBytes("post_image");

    return new Post(postId, userId, categoryId, title, content,postImage); // Передаємо postImage в конструктор Post
  }

}
