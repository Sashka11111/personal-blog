package com.liamtseva.persistence.repository.contract;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.User;
import java.util.List;
import javafx.scene.image.Image;

public interface UserRepository {
  void addUser(User user);

  User getUserById(int id) throws EntityNotFoundException;
  User findByUsername(String username) throws EntityNotFoundException;
  List<User> findByUsernameLike(String username);
  boolean isUsernameExists(String username);
  List<User> getAllUsers();
  void updateUser(User user) throws EntityNotFoundException;
  void deleteUser(int id) throws EntityNotFoundException;
}