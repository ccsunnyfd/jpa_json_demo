package com.tiger.jpa_json_demo.utils;

import com.tiger.jpa_json_demo.dao.UserDao;
import com.tiger.jpa_json_demo.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Util
 *
 * @version 1.0
 */
public class Util {
    public static UserInfo getCurrentUser() {
//        UserInfo user = new UserInfo();
//        user.setId(2L);
//        user.setNickname("tiger");
//        user.setUsername("tiger123");
//        user.setEmail("tiger@126.com");
//        user.setEnabled(1);
//        user.setPassword("43b90920409618f188bfc6923f16b9fa");
//        return user;
        return (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
