package com.tiger.jpa_json_demo.utils;

import com.tiger.jpa_json_demo.model.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Util
 *
 * @version 1.0
 */
public class Util {
    public static UserInfo getCurrentUser() {
        return (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
