package com.tiger.jpa_json_demo.dao;

import com.tiger.jpa_json_demo.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ArticleDao
 *
 * @version 1.0
 */
public interface ArticleDao  extends JpaRepository<Article, Long> {
    Article getArticleById(Long aid);
}
