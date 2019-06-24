package com.tiger.jpa_json_demo.controller;

import com.tiger.jpa_json_demo.model.RespBean;
import com.tiger.jpa_json_demo.model.UserInfo;
import com.tiger.jpa_json_demo.service.UserService;
import com.tiger.jpa_json_demo.utils.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * UserController
 *
 * @version 1.0
 */
@RestController
@Api(value = "用户信息的增删改查")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("currentUserNickname")
    @ApiOperation(value = "获取用户昵称")
    public String currentUserNickname() {
        return Util.getCurrentUser().getNickname();
    }

    @GetMapping("currentUserId")
    @ApiOperation(value = "获取当前用户Id")
    public Long currentUserId() {
        return Util.getCurrentUser().getId();
    }

    @GetMapping("currentUserEmail")
    @ApiOperation(value = "获取当前用户邮箱")
    public String currentUserEmail() {
        return Util.getCurrentUser().getEmail();
    }

    @GetMapping("isAdmin")
    @ApiOperation(value = "判断当前用户是否有管理员权限")
    public Boolean isAdmin() {
        List<GrantedAuthority> authorities = Util.getCurrentUser().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().contains("超级管理员")) {
                return true;
            }
        }
        return false;
    }


    // 如果使用@RequestBody，那么会自动将从前端传过来的json格式的参数封装为java bean对象，可以解决Postman在Post body中发送json格式的数据接收不到的问题
    // 如果不使用@RequestBody，那么vue-resource方式的前端脚本在跨域请求时需要加上{ emulateJSON: true }，使用了@RequestBody则要注释掉这个JSON模拟，否则浏览器会报415错误

    /**
     * 注册账号
     * api: localhost:8080/api/updateUserEmail
     *
     * @param email email
     */
    @PutMapping(path = "api/updateUserEmail")
    @ApiOperation(value = "更新账号邮箱")
    public RespBean updateUserEmail(String email) {
        userService.updateUserEmail(email);
        return new RespBean("success", "更新邮箱成功!");
    }
}
