package com.liamtseva.persistence.repository.contract;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Comment;
import java.util.List;

public interface CommentRepository {
  void addComment(Comment comment)throws EntityNotFoundException;
  Comment getCommentById(int id) throws EntityNotFoundException;
  List<Comment> getCommentsByPostId(int postId);
  List<Comment> getAllComments();
  void updateComment(Comment comment) throws EntityNotFoundException;
  void deleteComment(int id) throws EntityNotFoundException;
}