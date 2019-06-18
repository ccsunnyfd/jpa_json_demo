package com.tiger.jpa_json_demo.controller;

import com.tiger.jpa_json_demo.model.RespBean;
import com.tiger.jpa_json_demo.model.UserInfo;
import com.tiger.jpa_json_demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * LoginRegController
 *
 * @version 1.0
 */
@RestController
@Api(value = "article信息的增删改查")
public class LoginRegController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login_error")
    @ApiOperation(value = "登录失败消息")
    public RespBean loginError() {
        return new RespBean("error", "登录失败!");
    }

    @GetMapping("login_success")
    @ApiOperation(value = "登录成功消息")
    public RespBean loginSuccess() {
        return new RespBean("success", "登录成功!");
    }

    /**
     * 如果自动跳转到这个页面，说明用户未登录，返回相应的提示即可
     * <p>
     * 如果要支持表单登录，可以在这个方法中判断请求的类型，进而决定返回JSON还是HTML页面
     *
     * @return RespBean
     */
    @GetMapping("login_page")
    @ApiOperation(value = "尚未登录消息")
    public RespBean loginPage() {
        return new RespBean("error", "尚未登录，请登录!");
    }

// 如果使用@RequestBody，那么会自动将从前端传过来的json格式的参数封装为java bean对象，可以解决Postman在Post body中发送json格式的数据接收不到的问题
    // 如果不使用@RequestBody，那么vue-resource方式的前端脚本在跨域请求时需要加上{ emulateJSON: true }，使用了@RequestBody则要注释掉这个JSON模拟，否则浏览器会报415错误

    /**
     * 注册账号
     * api: localhost:8080/api/auth/reg
     *
     * @param user user
     */
    @PostMapping(path = "api/auth/reg")
    @ApiOperation(value = "注册账号")
    public RespBean reg(@RequestBody UserInfo user) {
        int result = userService.reg(user);
        if (result == 0) {
            return new RespBean("success", "注册成功");
        } else if (result == 1) {
            return new RespBean("error", "用户名重复，注册失败!");
        } else {
            return new RespBean("error", "注册失败!");
        }
    }
}
