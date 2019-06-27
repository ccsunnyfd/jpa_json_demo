package com.tiger.jpa_json_demo.controller;

import com.tiger.jpa_json_demo.model.Article;
import com.tiger.jpa_json_demo.model.RespBean;
import com.tiger.jpa_json_demo.service.ArticleService;
import com.tiger.jpa_json_demo.utils.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


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

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }
// 如果使用@RequestBody，那么会自动将从前端传过来的json格式的参数封装为java bean对象，可以解决Postman在Post body中发送json格式的数据接收不到的问题
    // 如果不使用@RequestBody，那么vue-resource方式的前端脚本在跨域请求时需要加上{ emulateJSON: true }，使用了@RequestBody则要注释掉这个JSON模拟，否则浏览器会报415错误


    /**
     * 管理员权限文章分页查询
     * api: localhost:8080/api/article/all
     *
     * @param page    第几页
     * @param size    每页几条记录
     * @param keyword 查询关键字
     * @return Map
     */
    @GetMapping("all")
    @ApiOperation(value = "查询当前用户article信息(state 0表示草稿箱，1表示已发表，2表示回收站, -1表示全部状态)")
    public Map<String, Object> getArticleByState(@RequestParam(value = "state", defaultValue = "-1") Integer state,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "6") Integer size,
                                                 @RequestParam(value = "keyword") String keyword) {
        Map<String, Object> map = new HashMap<>();
        List<Article> articles;
        Sort sort = new Sort(Sort.Direction.DESC, "editTime");
        int totalCount;
        Page<Article> pageArticles = articleService.getPageArticles(Util.getCurrentUser(), state == -1 ? null : state, keyword == null ? null : keyword.trim(), page, size, sort);
        articles = pageArticles.getContent();
        totalCount = (int) pageArticles.getTotalElements();
        map.put("articles", articles);
        map.put("totalCount", totalCount);
        return map;
    }


    /**
     * 添加指定person
     * api: localhost:8080/api/article/addArticle
     *
     * @param article article
     */
    @PostMapping(path = "addArticle")
    @ApiOperation(value = "添加或更新指定article")
    public RespBean addArticle(@RequestBody Article article) {
        Long result = articleService.addArticle(article);
        if (result != null) {
            return new RespBean("success", result + "");
        } else {
            return new RespBean("error", article.getState() == 0 ? "文章保存失败!" : "文章发表失败!");
        }
    }


    /**
     * 把文件存放到网站路径http://**.**.**.**:8080/(ContextPath)/blogimg/20190625/
     *
     * @param req   req
     * @param image image
     * @return 返回值为图片的地址
     */
    @PostMapping(path = "uploadimg")
    @ApiOperation(value = "上传图片")
    public RespBean uploadImg(HttpServletRequest req, MultipartFile image) {
        StringBuilder url = new StringBuilder();

        // 创建存放图片的文件夹(如果不存在的话)
        String filePath = "/blogimg/" + sdf.format(new Date());
        String imgFolderPath = req.getServletContext().getRealPath(filePath);
        File imgFolder = new File(imgFolderPath);
        if (!imgFolder.exists()) {
            imgFolder.mkdirs();
        }

        url.append(req.getScheme())
                .append("://")
                .append(req.getServerName())
                .append(":")
                .append(req.getServerPort())
                .append(req.getContextPath())
                .append(filePath);
        String imgName = UUID.randomUUID() + "_" + image.getOriginalFilename().replaceAll(" ", "");
        try {
            IOUtils.write(image.getBytes(), new FileOutputStream(new File(imgFolder, imgName)));
            url.append("/").append(imgName);
            return new RespBean("success", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RespBean("error", "上传失败!");
    }


    /**
     * 根据id查询article信息
     * api: localhost:8080/api/article/{aid}
     *
     * @param aid aid
     * @return Article
     */
    @GetMapping("{aid}")
    @ApiOperation(value = "根据id查询article信息")
    public Article getArticleById(@PathVariable Long aid) {
        return articleService.getArticleById(aid);
    }


    /**
     * 回收或彻底删除Article
     * api: localhost:8080/api/article/dustbin
     *
     * @param aids  文章id组
     * @param state 2:对已存在回收站中的文章彻底删除  其他：先放入回收站
     */
    @PutMapping(path = "dustbin")
    @ApiOperation(value = "文章回收站处理机制")
    public RespBean cycleArticle(@RequestParam("aids") Long[] aids, @RequestParam("state") Integer state) {
        articleService.cycleArticle(aids, state);
        return new RespBean("success", "已删除!");
    }
}
