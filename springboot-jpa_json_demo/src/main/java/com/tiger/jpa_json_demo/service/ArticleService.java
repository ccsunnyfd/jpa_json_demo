package com.tiger.jpa_json_demo.service;

import com.tiger.jpa_json_demo.dao.ArticleDao;
import com.tiger.jpa_json_demo.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ArticleService
 *
 * @version 1.0
 */
@Service
public class ArticleService {
    private ArticleDao articleDao;

    @Autowired
    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    @Transactional
    public Long addArticle(Article article) {
        //处理文章摘要
        if (article.getSummary() == null || "".equals(article.getSummary())) {
            //直接截取
            String stripHtml = stripHtml(article.getHtmlContent());
            article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }
        Article retArticle = articleDao.save(article);
        return retArticle.getId();
    }

    public Article getArticleById(Long aid) {
        //articleDao.pvIncrement(aid);
        return articleDao.getArticleById(aid);
    }

    private String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("<.*?>", "");
        return content;
    }
}
