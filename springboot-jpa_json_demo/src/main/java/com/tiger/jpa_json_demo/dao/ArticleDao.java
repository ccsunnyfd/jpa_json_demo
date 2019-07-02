package com.tiger.jpa_json_demo.dao;

import com.tiger.jpa_json_demo.model.Article;
//import com.tiger.jpa_json_demo.model.UserInfo;
//import org.apache.catalina.User;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * ArticleDao
 *
 * @version 1.0
 */
public interface ArticleDao  extends JpaRepository<Article, Long>, JpaSpecificationExecutor {
    Article getArticleById(Long aid);

//    @Query(value = "select a from Article a where a.user=:user and a.state=:state and a.title LIKE CONCAT('%',:keyword,'%') order by a.editTime desc")
//    Page<Article> getArticlesByStateAndKeywordByUidPageable(@Param("user")UserInfo user, @Param("state") Integer state, @Param("keyword") String keyword, Pageable pageable);
//
//    @Query(value = "select a from Article a where a.user=:user and a.state=:state order by a.editTime desc")
//    Page<Article> getArticlesByStateByUidPageable(@Param("user")UserInfo user, @Param("state") Integer state, Pageable pageable);
//
//    @Query(value = "select a from Article a where a.state=:state and a.title LIKE CONCAT('%',:keyword,'%') order by a.editTime desc")
//    Page<Article> getArticlesByStateAndKeywordPageable(@Param("state") Integer state, @Param("keyword") String keyword, Pageable pageable);
//
//    @Query(value = "select a from Article a where a.state=:state order by a.editTime desc")
//    Page<Article> getArticlesByStatePageable(@Param("state") Integer state, Pageable pageable);
//
//    @Query(value = "select count(a) from Article a where a.user=:user and a.state=:state and a.title LIKE CONCAT('%',:keyword,'%')")
//    int getArticleCountByStateAndKeywordByUid(@Param("user")UserInfo user, @Param("state") Integer state, @Param("keyword") String keyword);
//
//    @Query(value = "select count(a) from Article a where a.user=:user and a.state=:state")
//    int getArticleCountByStateByUid(@Param("user")UserInfo user, @Param("state") Integer state);
//
//    @Query(value = "select count(a) from Article a where a.state=:state and a.title LIKE CONCAT('%',:keyword,'%')")
//    int getArticleCountByStateAndKeyword(@Param("state") Integer state, @Param("keyword") String keyword);
//
//    @Query(value = "select count(a) from Article a where a.state=:state")
//    int getArticleCountByState(@Param("state") Integer state);

    @Modifying
    @Query(value = "update Article a set a.state = :state where a.id = :aid")
    void updateArticleState(@Param("aid") Long aid, @Param("state") Integer state);

    @Modifying
    @Query(value = "update Article a set a.pageView = a.pageView+1 where a.id = :aid")
    void pvIncrement(@Param("aid") Long aid);

    /**
     * DROP TABLE IF EXISTS `pv`;
     * CREATE TABLE `pv` (
     *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
     *   `countDate` date DEFAULT NULL,
     *   `pv` int(11) DEFAULT NULL,
     *   `uid` bigint(20) DEFAULT NULL,
     *   PRIMARY KEY (`id`),
     *   KEY `pv_ibfk_1` (`uid`),
     *   CONSTRAINT `pv_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `userinfo` (`id`) ON DELETE CASCADE
     * ) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

     -- ----------------------------
     -- View structure for pvview
     -- ----------------------------
     DROP VIEW IF EXISTS `pvview`;
     CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost`  VIEW `pvview` AS select sum(pv) as pv,uid from pv group by uid ;

     -- ----------------------------
     -- View structure for totalpvview
     -- ----------------------------
     DROP VIEW IF EXISTS `totalpvview`;
     CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost`  VIEW `totalpvview` AS select sum(page_view) as totalPv,user_id from article a group by user_id ;
     SET FOREIGN_KEY_CHECKS=1;

     */


    //pvview视图保存从pv每日计算的总的分类统计累加，totalpvview视图保存从article表收集的分类统计累加，totalPv - pv正好是每日新增量
    @Query(value = "INSERT INTO pv(countDate,pv,uid) \n" +
            "SELECT CURRENT_DATE(),totalPv - pv, t.user_id FROM pvview p,\n" +
            "totalpvview t \n" +
            "WHERE p.uid =t.user_id", nativeQuery = true)
    void pvStatisticsPerDay();

    //获得最近七日的pv横轴数据--日期
    @Query(value = "SELECT countDate from pv WHERE uid=?1 ORDER by countDate DESC limit 7", nativeQuery = true)
    List<String> getCategories(Long uid);

    //获得最近七日的pv纵轴数据--pv
    @Query(value = "SELECT pv from pv WHERE uid=?1 ORDER by countDate DESC limit 7", nativeQuery = true)
    List<Integer> getDataStatistics(Long uid);

//
//    @Query(value = "select a from Article a where a.state=:state and a.title LIKE CONCAT('%',:keyword,'%') order by a.editTime desc")
//    Page<Article> getArticlesByStateAndKeywordPageableByAdmin(@Param("state") Integer state, @Param("keyword") String keyword, Pageable pageable);
//
//    @Query(value = "select a from Article a where a.state=:state order by a.editTime desc")
//    Page<Article> getArticlesByStatePageableByAdmin(@Param("state") Integer state, Pageable pageable);

    @Query(value = "select tag_id from article_tag where article_id = ?1", nativeQuery = true)
    List<Long> getTagIdsFromArticle(Long aid);


    @Query(value = "insert into article_tag(tag_id, article_id) values(?1, ?2)", nativeQuery = true)
    void addTagToArticle(Long tid, Long aid);

    @Modifying
    @Query(value = "delete from article_tag where article_id = ?2", nativeQuery = true)
    void removeAllTagsFromArticle(Long aid);
}
