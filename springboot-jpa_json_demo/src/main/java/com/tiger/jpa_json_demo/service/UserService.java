package com.tiger.jpa_json_demo.service;

import com.tiger.jpa_json_demo.dao.RolesDao;
import com.tiger.jpa_json_demo.dao.UserDao;
import com.tiger.jpa_json_demo.model.RoleInfo;
import com.tiger.jpa_json_demo.model.UserInfo;
import com.tiger.jpa_json_demo.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashSet;
import java.util.List;

/**
 * UserService
 *
 * @version 1.0
 */
@Service
public class UserService implements UserDetailsService {
    private RolesDao rolesDao;
    private UserDao userDao;

    @Autowired
    public void setRolesDao(RolesDao rolesDao) {
        this.rolesDao = rolesDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserInfo user = userDao.findUserInfoByUsername(s);
        if (user == null) {
            return new UserInfo();
        }
        return user;
    }

    /**
     * registration
     * @param user UserInfo
     * @return
     * 0: success
     * 1: duplicate username
     * 2: registration failure
     */
    @Transactional
    public int reg(UserInfo user) {
        UserInfo loadUserByUsername = userDao.findUserInfoByUsername(user.getUsername());
        if (loadUserByUsername != null) {
            return 1;   //    已经存在该用户
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setEnabled(1);

        //配置用户角色，默认都是普通用户 id=2
        RoleInfo role = new RoleInfo();
        role.setId((long)2);
        HashSet<RoleInfo> roleSet = new HashSet<>();
        roleSet.add(role);
        user.setRoles(roleSet);

        Long uid = userDao.save(user).getId();
        if (uid != null) {
            return 0;
        }
        return 2;
    }

    @Transactional
    public void updateUserEmail(String email) {
        userDao.updateEmailById(email, Util.getCurrentUser().getId());
    }

    public List<UserInfo> getUserByNickname(String nickname) {
        return userDao.getUserInfoByNickname(nickname);
    }

    public List<RoleInfo> getAllRoles() {
        return rolesDao.findAll();
    }

    // 1:enabled 0:disabled
    @Transactional
    public void updateUserEnabled(Integer enabled, Long uid) {
        userDao.updateUserEnabled(enabled, uid);
    }

    @Transactional
    public void deleteUserById(Long id) {
        userDao.deleteById(id);
    }

    @Transactional
    public void updateUserRoles(Long[] rids, Long uid) {
        userDao.deleteUserRolesByUid(uid);
        for (Long rid : rids
        ) {
            userDao.addUserRole(rid, uid);
        }
    }

    public UserInfo getUserById(Long uid) {
        return userDao.getUserInfoById(uid);
    }
}
