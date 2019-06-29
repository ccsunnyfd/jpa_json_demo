package com.tiger.jpa_json_demo.dao;

import com.tiger.jpa_json_demo.model.RoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * RolesDao
 *
 * @version 1.0
 */
public interface RolesDao extends JpaRepository<RoleInfo, Long> {
    List<RoleInfo> getRolesById(Long id);
}
