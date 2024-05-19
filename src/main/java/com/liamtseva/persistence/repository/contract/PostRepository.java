package com.liamtseva.persistence.repository.contract;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Post;
import java.util.List;

public interface PostRepository {
  void addPost(Post post);
  Post getPostById(int id) throws EntityNotFoundException;
  Post getPostByName(String name) throws EntityNotFoundException;
  List<Post> getAllPostsByUserId(int userId);
  List<Post> getAllPosts();
  List<Post> filterPostsByUserId(int userId);
  void updatePost(Post post) throws EntityNotFoundException;
  void deletePost(int id) throws EntityNotFoundException;
}