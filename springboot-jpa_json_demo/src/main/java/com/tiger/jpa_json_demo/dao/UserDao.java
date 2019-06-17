package com.tiger.jpa_json_demo.dao;

import com.tiger.jpa_json_demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * UserDao
 *
 * @version 1.0
 */
public interface UserDao extends JpaRepository<UserInfo, Long> {
    UserInfo findUserInfoByUsername(String username);

    List<UserInfo> getUserInfoByNickname(String nickname);

    UserInfo getUserInfoById(Long id);

    @Modifying
    @Query("update UserInfo u set u.email = :email where u.id = :id")
    void updateEmailById(@Param("email") String email, @Param("id") Long id);

    @Modifying
    @Query("update UserInfo u set u.enabled = :enabled where u.id = :id")
    void updateUserEnabled(@Param("enabled") Integer enabled, @Param("id") Long id);

    @Modifying
    @Query(value = "delete from user_role where ur.user_id = :id", nativeQuery = true)
    void deleteUserRolesByUid(@Param("id") Long id);

    @Query(value = "insert into user_role(role_id,user_id) values(:rid, :uid)", nativeQuery = true)
    void addUserRole(@Param("rid") Long rid, @Param("uid") Long uid);
}
