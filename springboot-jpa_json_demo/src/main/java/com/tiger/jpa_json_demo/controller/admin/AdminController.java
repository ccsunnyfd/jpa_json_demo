package com.tiger.jpa_json_demo.controller.admin;

import com.tiger.jpa_json_demo.model.Article;
import com.tiger.jpa_json_demo.model.RespBean;
import com.tiger.jpa_json_demo.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * AdminController
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin")
@Api(value = "超级管理员专属Controller")
public class AdminController {
    private ArticleService articleService;

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }


    /**
     * 管理员权限文章分页查询
     * api: localhost:8080/api/admin/article/all
     *
     * @param page    第几页
     * @param size    每页几条记录
     * @param keyword 查询关键字
     * @return Map
     */
    @GetMapping("article/all")
    @ApiOperation(value = "查询所有article信息(state 0表示草稿箱，1表示已发表，2表示回收站, -1表示全部状态)")
    public Map<String, Object> getArticleByStateByAdmin(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                        @RequestParam(value = "size", defaultValue = "6") Integer size,
                                                        @RequestParam(value = "keyword") String keyword) {
        Map<String, Object> map = new HashMap<>();
        List<Article> articles;
        Sort sort = new Sort(Sort.Direction.DESC, "editTime");
        int totalCount = 0;
        Page<Article> pageArticles = articleService.getPageArticles(null, null, keyword == null ? null : keyword.trim(), page, size, sort);
        articles = pageArticles.getContent();
        totalCount = (int) pageArticles.getTotalElements();
        map.put("articles", articles);
        map.put("totalCount", totalCount);
        return map;
    }


// 如果使用@RequestBody，那么会自动将从前端传过来的json格式的参数封装为java bean对象，可以解决Postman在Post body中发送json格式的数据接收不到的问题
    // 如果不使用@RequestBody，那么vue-resource方式的前端脚本在跨域请求时需要加上{ emulateJSON: true }，使用了@RequestBody则要注释掉这个JSON模拟，否则浏览器会报415错误

    /**
     * 回收或彻底删除Article
     * api: localhost:8080/api/admin/article/dustbin
     *
     * @param aids  文章id组
     * @param state 2:对已存在回收站中的文章彻底删除  其他：先放入回收站
     */
    @PutMapping(path = "article/dustbin")
    @ApiOperation(value = "文章回收站处理机制")
    public RespBean cycleArticle(@RequestBody Long[] aids, @RequestBody Integer state) {
        articleService.cycleArticle(aids, state);
        return new RespBean("success", "已删除!");
    }

}
