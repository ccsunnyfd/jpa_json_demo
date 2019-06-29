package com.tiger.jpa_json_demo.dao;

import com.tiger.jpa_json_demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * UserDao
 *
 * @version 1.0
 */
public interface UserDao extends JpaRepository<UserInfo, Long>, JpaSpecificationExecutor {
    UserInfo findUserInfoByUsername(String username);

    UserInfo getUserInfoById(Long uid);

    @Modifying
    @Query("update UserInfo u set u.email = :email where u.id = :uid")
    void updateEmailById(@Param("email") String email, @Param("uid") Long uid);

    @Modifying
    @Query("update UserInfo u set u.enabled = :enabled where u.id = :uid")
    void updateUserEnabled(@Param("enabled") Integer enabled, @Param("uid") Long uid);

    @Modifying
    @Query(value = "delete from user_role  where user_id = ?1", nativeQuery = true)
    void deleteUserRolesByUid(Long uid);

    @Modifying
    @Query(value = "insert into user_role(role_id, user_id) values(?1, ?2)", nativeQuery = true)
    void addUserRole(Long rid, Long uid);
}
