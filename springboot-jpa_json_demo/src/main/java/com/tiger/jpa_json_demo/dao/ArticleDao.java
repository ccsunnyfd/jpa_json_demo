package com.tiger.jpa_json_demo.dao;

import com.tiger.jpa_json_demo.model.Article;
import com.tiger.jpa_json_demo.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * ArticleDao
 *
 * @version 1.0
 */
public interface ArticleDao  extends JpaRepository<Article, Long> {
    Article getArticleById(Long aid);

    @Query(value = "select a from Article a where a.user=:user and a.state=:state and a.title LIKE CONCAT('%',:keyword,'%') order by a.editTime desc")
    Page<Article> getArticlesByStateAndKeywordByUidPageable(@Param("user")UserInfo user, @Param("state") Integer state, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select a from Article a where a.user=:user and a.state=:state order by a.editTime desc")
    Page<Article> getArticlesByStateByUidPageable(@Param("user")UserInfo user, @Param("state") Integer state, Pageable pageable);

    @Query(value = "select a from Article a where a.state=:state and a.title LIKE CONCAT('%',:keyword,'%') order by a.editTime desc")
    Page<Article> getArticlesByStateAndKeywordPageable(@Param("state") Integer state, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select a from Article a where a.state=:state order by a.editTime desc")
    Page<Article> getArticlesByStatePageable(@Param("state") Integer state, Pageable pageable);

    @Query(value = "select count(a) from Article a where a.user=:user and a.state=:state and a.title LIKE CONCAT('%',:keyword,'%')")
    int getArticleCountByStateAndKeywordByUid(@Param("user")UserInfo user, @Param("state") Integer state, @Param("keyword") String keyword);

    @Query(value = "select count(a) from Article a where a.user=:user and a.state=:state")
    int getArticleCountByStateByUid(@Param("user")UserInfo user, @Param("state") Integer state);

    @Query(value = "select count(a) from Article a where a.state=:state and a.title LIKE CONCAT('%',:keyword,'%')")
    int getArticleCountByStateAndKeyword(@Param("state") Integer state, @Param("keyword") String keyword);

    @Query(value = "select count(a) from Article a where a.state=:state")
    int getArticleCountByState(@Param("state") Integer state);

    @Modifying
    @Query(value = "update Article a set a.state = :state where a.id = :aid")
    void updateArticleState(@Param("aid") Long aid, @Param("state") Integer state);

    @Modifying
    @Query(value = "update Article a set a.pageView = a.pageView+1 where a.id = :aid")
    void pvIncrement(@Param("aid") Long aid);


//
//    @Query(value = "select a from Article a where a.state=:state and a.title LIKE CONCAT('%',:keyword,'%') order by a.editTime desc")
//    Page<Article> getArticlesByStateAndKeywordPageableByAdmin(@Param("state") Integer state, @Param("keyword") String keyword, Pageable pageable);
//
//    @Query(value = "select a from Article a where a.state=:state order by a.editTime desc")
//    Page<Article> getArticlesByStatePageableByAdmin(@Param("state") Integer state, Pageable pageable);

    @Query(value = "insert into article_tag(tag_id, article_id) values(?1, ?2)", nativeQuery = true)
    void addTagToArticle(Long tid, Long aid);

    @Modifying
    @Query(value = "delete from article_tag where tag_id = ?1 and article_id = ?2", nativeQuery = true)
    void removeTagFromArticle(Long tid, Long aid);
}
