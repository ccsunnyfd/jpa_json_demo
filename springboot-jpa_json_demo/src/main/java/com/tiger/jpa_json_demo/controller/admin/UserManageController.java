package com.tiger.jpa_json_demo.controller.admin;

import com.tiger.jpa_json_demo.model.RespBean;
import com.tiger.jpa_json_demo.model.RoleInfo;
import com.tiger.jpa_json_demo.model.UserInfo;
import com.tiger.jpa_json_demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * UserManageController
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/admin")
@Api(value = "超级管理员用户管理")
public class UserManageController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根据昵称查询用户信息
     * api: localhost:8080/api/admin/user?nickname=XXX
     *
     * @param nickname 昵称
     * @return 用户信息
     */
    @GetMapping("user")
    @ApiOperation(value = "根据昵称查询用户信息")
    public List<UserInfo> getUserByNickname(@RequestParam(value = "nickname") String nickname) {
        return userService.getUserByNickname(nickname);
    }

    /**
     * 根据用户id查询用户信息
     * api: localhost:8080/api/admin/user/{id}
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("user/{id}")
    @ApiOperation(value = "根据用户id查询用户信息")
    public UserInfo getUserById(@RequestParam(value = "id") Long id) {
        return userService.getUserById(id);
    }

    /**
     * 获取所有角色信息
     * api: localhost:8080/api/admin/roles
     *
     * @return 角色信息
     */
    @GetMapping("roles")
    @ApiOperation(value = "获取所有角色信息")
    public List<RoleInfo> getAllRoles() {
        return userService.getAllRoles();
    }

    //1:enabled 0:disabled

    /**
     * 更新用户账号状态
     * api: localhost:8080/api/admin/user/enabled
     *
     * @param enabled 用户账号有效状态
     * @param uid     用户账号id
     * @return RespBean
     */
    @PutMapping("user/enabled")
    @ApiOperation(value = "更新用户账号状态")
    public RespBean updateUserEnabled(Integer enabled, Long uid) {
        userService.updateUserEnabled(enabled, uid);
        return new RespBean("success", "更新用户账号状态成功!");
    }

    /**
     * 更新用户角色
     * api: localhost:8080/api/admin/user/role
     *
     * @param rids 用户角色Id组
     * @param uid  用户账号id
     * @return RespBean
     */
    @PutMapping("user/role")
    @ApiOperation(value = "更新用户角色")
    public RespBean updateUserRoles(Long[] rids, Long uid) {
        userService.updateUserRoles(rids, uid);
        return new RespBean("success", "更新用户角色成功!");
    }
}
