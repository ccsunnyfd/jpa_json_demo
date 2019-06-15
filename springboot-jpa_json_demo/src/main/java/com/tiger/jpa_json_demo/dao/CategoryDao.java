package com.tiger.jpa_json_demo.dao;

import com.tiger.jpa_json_demo.model.CategoryInfo;
import com.tiger.jpa_json_demo.model.TagInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * CategoryDao
 *
 * @version 1.0
 */
public interface CategoryDao extends JpaRepository<CategoryInfo, Long> {
    List<CategoryInfo> findAll();
    CategoryInfo getCategoryInfoById(Long cid);
}
