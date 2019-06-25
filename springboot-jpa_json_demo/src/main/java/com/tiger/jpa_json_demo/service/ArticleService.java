package com.tiger.jpa_json_demo.service;

import com.google.common.collect.Lists;
import com.tiger.jpa_json_demo.dao.ArticleDao;
import com.tiger.jpa_json_demo.dao.TagDao;
import com.tiger.jpa_json_demo.model.Article;
import com.tiger.jpa_json_demo.model.TagInfo;
import com.tiger.jpa_json_demo.model.UserInfo;
import com.tiger.jpa_json_demo.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ArticleService
 *
 * @version 1.0
 */
@Service
public class ArticleService {
    private ArticleDao articleDao;
    private TagDao tagDao;

    @Autowired
    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    //article.id:
    // -1:添加操作
    // 其他：更新操作
    @Modifying
    @Transactional
    public Long addArticle(Article article, String[] dynamicTags) {
        //处理文章摘要
        if (article.getSummary() == null || "".equals(article.getSummary())) {
            //直接截取
            String stripHtml = stripHtml(article.getHtmlContent());
            article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }
        Date publishTime = new Date();
        if (article.getId() == -1) {  // -1表示是新增操作
            //新增操作
            if (article.getState() == 1) {  // 1表示如果是要直接发表
                //设置发表时间
                article.setPublishTime(publishTime);
            }
            //设置当前用户
            article.setUser(Util.getCurrentUser());
            Long i = articleDao.save(article).getId();

            //更新标签
            if (dynamicTags != null && dynamicTags.length > 0) {
                int tags = addTagsToArticle(dynamicTags, article.getId());
                if (tags == -1) {
                    return tags;
                }
            }
        } else {
            //更新操作
            if (article.getState() == 1) {   // 1表示如果是要直接发表
                //设置发表时间
                article.setPublishTime(publishTime);
            }
            //更新
            Article oldArticle = articleDao.getArticleById(article.getId());
            oldArticle.setCategory(article.getCategory());
            oldArticle.setSummary(article.getSummary());
            oldArticle.setTitle(article.getTitle());
            oldArticle.setPublishTime(article.getPublishTime());
            oldArticle.setMdContent(article.getMdContent());
            oldArticle.setHtmlContent(article.getHtmlContent());
            oldArticle.setState(article.getState());
            oldArticle.setTags(article.getTags());
            return articleDao.save(oldArticle).getId();
        }
    }

    private Boolean inList(List<String> arr, String target) {
        for (String a : arr
        ) {
            if (a.equals(target)) {
                return true;
            }
        }
        return false;
    }


    private int addTagsToArticle(String[] dynamicTags, Long aid) {

        //1.查询该文章目前所有关联的标签（中间表）
        List<Long> tIds = articleDao.getTagIdsFromArticle(aid);
        List<TagInfo> currentTags = tagDao.findAllById(tIds);
        List<String> currentTagNames = currentTags.stream().map((x) -> x.getTagName()).collect(Collectors.toList());

        //2. 构建还没有关联的标签
        List<TagInfo> tags = new ArrayList<>();
        for (String tag : dynamicTags
        ) {
            if (!inList(currentTagNames, tag)){
                TagInfo tagItem = new TagInfo();
                tagItem.setTagName(tag);
                tags.add(tagItem);
            }

        }

        //3. 寻找已经被取消关联的标签


        List<TagInfo> newTags = tagDao.saveAll(tags);


        articleDao.removeAllTagsFromArticle(aid);


        //3.将上传上来的标签全部存入数据库
        List<TagInfo> tags = new ArrayList<>();


        //4.查询这些标签的id
        List<Long> tIds = newTags.stream().map((x) -> x.getId()).collect(Collectors.toList());

        //4.重新给文章设置标签
        int i = articleDao. (tIds, aid);
        return i == dynamicTags.length ? i : -1;
    }


    ;

    @Transactional
    public void addTagToArticle(Long tid, Long aid) {
        articleDao.addTagToArticle(tid, aid);
    }

    @Transactional
    public void removeTagFromArticle(Long tid, Long aid) {
        articleDao.removeTagFromArticle(tid, aid);
    }

    @Transactional
    public Article getArticleById(Long aid) {
        Article article = articleDao.getArticleById(aid);
        articleDao.pvIncrement(aid);
        return article;
    }

    private String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("<.*?>", "");
        return content;
    }

    // 注意，PageRequest.of的第一个参数表示第几页，从0开始计数，这与通常的分页从1开始有些不同，要处理下
    //动态构造查询语句
    public Page<Article> getPageArticles(UserInfo user, Integer state, String keyword, Integer pageNum, Integer
            size, Sort sort) {
        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();
                if (!StringUtils.isEmpty(user)) {
                    predicates.add(criteriaBuilder.equal(root.get("user"), user));
                }
                if (!StringUtils.isEmpty(state)) {
                    predicates.add(criteriaBuilder.equal(root.get("state"), state));
                }
                if (!StringUtils.isEmpty(keyword)) {
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + keyword + "%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<Article> page = articleDao.findAll(querySpeci, PageRequest.of(pageNum - 1, size, sort));
        return page;
    }


//    public List<Article> getArticleByStateAndKeywordByUid(Integer state, Integer page, Integer size, Sort sort, String keyword) {
//        return articleDao.getArticlesByStateAndKeywordByUidPageable(Util.getCurrentUser(), state, keyword, PageRequest.of(page - 1, size, sort)).getContent();
//    }
//
//    public List<Article> getArticleByStateByUid(Integer state, Integer page, Integer size, Sort sort) {
//        return articleDao.getArticlesByStateByUidPageable(Util.getCurrentUser(), state, PageRequest.of(page - 1, size, sort)).getContent();
//    }
//
//    public List<Article> getArticleByStateAndKeyword(Integer state, Integer page, Integer size, Sort sort, String keyword) {
//        return articleDao.getArticlesByStateAndKeywordPageable(state, keyword, PageRequest.of(page - 1, size, sort)).getContent();
//    }
//
//    public List<Article> getArticleByState(Integer state, Integer page, Integer size, Sort sort) {
//        return articleDao.getArticlesByStatePageable(state, PageRequest.of(page - 1, size, sort)).getContent();
//    }
//
//    public int getArticleCountByStateAndKeywordByUid(Integer state, String keywords) {
//        return articleDao.getArticleCountByStateAndKeywordByUid(Util.getCurrentUser(), state, keywords);
//    }
//
//    public int getArticleCountByStateByUid(Integer state) {
//        return articleDao.getArticleCountByStateByUid(Util.getCurrentUser(), state);
//    }
//
//    public int getArticleCountByStateAndKeyword(Integer state, String keywords) {
//        return articleDao.getArticleCountByStateAndKeyword(state, keywords);
//    }
//
//    public int getArticleCountByState(Integer state) {
//        return articleDao.getArticleCountByState(state);
//    }

//
//    public List<Article> getArticleByStateAndKeywordByAdmin(Integer state, Integer page, Integer size, String keyword) {
//        return articleDao.getArticlesByStateAndKeywordPageable(state, keyword, PageRequest.of(page-1, size)).getContent();
//    }
//
//    public List<Article> getArticleByStateByAdmin(Integer state, Integer page, Integer size) {
//        return articleDao.getArticlesByStatePageable(state, PageRequest.of(page-1, size)).getContent();
//    }


    // 放入回收站或从回收站删除
    @Transactional
    public void cycleArticle(Long[] aids, Integer state) {
        if (state == 2) {  //在回收站界面做操作
            for (Long aid : aids
            ) {
                articleDao.deleteById(aid);
            }
        } else {    //在正常页面操作，准备放入回收站
            for (Long aid : aids
            ) {
                articleDao.updateArticleState(aid, 2);
            }
        }
    }


}
