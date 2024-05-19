package com.liamtseva.persistence.repository.impl;

import com.liamtseva.persistence.entity.Tag;
import com.liamtseva.persistence.repository.contract.TagsRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class TagsRepositoryImpl implements TagsRepository {

  private DataSource dataSource;

  public TagsRepositoryImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public List<Tag> getAllTags() {
    List<Tag> tags = new ArrayList<>();
    String query = "SELECT * FROM Tag";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery()) {

      while (resultSet.next()) {
        Tag tag = mapTag(resultSet);
        tags.add(tag);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tags;
  }
  private Tag mapTag(ResultSet resultSet) throws SQLException {
    return new Tag(
        resultSet.getInt("id_tip"),
        resultSet.getString("name")
    );
  }
}
