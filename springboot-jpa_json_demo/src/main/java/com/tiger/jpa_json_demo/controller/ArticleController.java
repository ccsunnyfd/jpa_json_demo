package com.tiger.jpa_json_demo.controller;

import com.tiger.jpa_json_demo.model.Article;
import com.tiger.jpa_json_demo.model.RespBean;
import com.tiger.jpa_json_demo.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * ArticleController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/article")
@Api(value = "article信息的增删改查")
public class ArticleController {
    private ArticleService articleService;

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
// 如果使用@RequestBody，那么会自动将从前端传过来的json格式的参数封装为java bean对象，可以解决Postman在Post body中发送json格式的数据接收不到的问题
    // 如果不使用@RequestBody，那么vue-resource方式的前端脚本在跨域请求时需要加上{ emulateJSON: true }，使用了@RequestBody则要注释掉这个JSON模拟，否则浏览器会报415错误

    /**
     * 添加指定person
     * api: localhost:8080/api/article/addArticle
     *
     * @param article article
     */
    @PostMapping(path = "addArticle")
    @ApiOperation(value = "添加指定article")
    public RespBean addArticle(@RequestBody Article article) {
        Long result = articleService.addArticle(article);
        if (result != null) {
            return new RespBean("success", result + "");
        } else {
            return new RespBean("error", article.getState() == 0 ? "文章保存失败!" : "文章发表失败!");
        }
    }

     /**
     * 根据id查询article信息
     * api: localhost:8080/api/article/getArticleById?aid=xxx
     * @param aid aid
     * @return Article
     */
    @GetMapping("getArticleById")
    @ApiOperation(value = "根据id查询article信息")
    public Article getArticleById(@RequestParam("aid") Long aid) {
        return articleService.getArticleById(aid);
    }
}
