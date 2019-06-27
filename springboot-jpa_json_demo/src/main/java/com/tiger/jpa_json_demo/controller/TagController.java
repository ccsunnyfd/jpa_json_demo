package com.tiger.jpa_json_demo.controller;

import com.tiger.jpa_json_demo.model.TagInfo;
import com.tiger.jpa_json_demo.model.RespBean;
import com.tiger.jpa_json_demo.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * TagController
 *
 * @version 1.0
 */
//@RestController
//@RequestMapping(value = "api/tag")
//@Api(value = "tag信息的增删改查")
//public class TagController {
//    private TagService tagService;
//
//    @Autowired
//    public void setTagService(TagService tagService) {
//        this.tagService = tagService;
//    }
//// 如果使用@RequestBody，那么会自动将从前端传过来的json格式的参数封装为java bean对象，可以解决Postman在Post body中发送json格式的数据接收不到的问题
//    // 如果不使用@RequestBody，那么vue-resource方式的前端脚本在跨域请求时需要加上{ emulateJSON: true }，使用了@RequestBody则要注释掉这个JSON模拟，否则浏览器会报415错误
//
//    /**
//     * 添加指定tag
//     * api: localhost:8080/api/tag/saveTag
//     *
//     * @param tag tag
//     */
//    @PostMapping(path = "saveTag")
//    @ApiOperation(value = "添加或更新指定tag")
//    public RespBean addTag(@RequestBody TagInfo tag) {
//        Long result = tagService.saveTag(tag);
//        if (result != null) {
//            return new RespBean("success", result + "");
//        } else {
//            return new RespBean("error", "标签保存失败!");
//        }
//    }
//
//    /**
//     * 根据id查询tag信息
//     * api: localhost:8080/api/tag/getTagById?tid=xxx
//     *
//     * @param tid tid
//     * @return tag
//     */
//    @GetMapping("getTagById")
//    @ApiOperation(value = "根据id查询tag信息")
//    public TagInfo getTagById(@RequestParam("tid") Long tid) {
//        return tagService.getTagById(tid);
//    }
//}
