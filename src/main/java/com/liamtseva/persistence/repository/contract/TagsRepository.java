package com.liamtseva.persistence.repository.contract;

import com.liamtseva.persistence.entity.Tag;
import java.util.List;

public interface TipsRepository {
  List<Tag> getAllTips();

}