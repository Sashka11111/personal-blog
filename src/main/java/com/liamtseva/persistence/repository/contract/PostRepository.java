package com.liamtseva.persistence.repository.contract;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Post;
import java.util.List;

public interface GoalRepository {
  void addGoal(Post goal);
  Post getGoalById(int id) throws EntityNotFoundException;
  Post getGoalByName(String name) throws EntityNotFoundException;
  List<Post> getAllGoalsByUserId(int userId);
  List<Post> getAllGoals();
  List<Post> filterGoalsByUserId(int userId);
  void updateGoal(Post goal) throws EntityNotFoundException;
  void deleteGoal(int id) throws EntityNotFoundException;
  void updateGoalStatus(int goalId, String newStatus) throws EntityNotFoundException;
  void updateGoalStatusByName(String goalName, String newStatus) throws EntityNotFoundException;
}