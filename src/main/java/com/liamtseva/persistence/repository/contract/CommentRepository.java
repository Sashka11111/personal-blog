package com.liamtseva.persistence.repository.contract;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Step;
import java.util.List;

public interface StepRepository {
  void addStep(Step step)throws EntityNotFoundException;
  Step getStepById(int id) throws EntityNotFoundException;
  List<Step> getStepsByGoalId(int goalId);
  List<Step> getAllSteps();
  void updateStep(Step step) throws EntityNotFoundException;
  void deleteStep(int id) throws EntityNotFoundException;
}