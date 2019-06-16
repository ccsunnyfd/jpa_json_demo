package com.tiger.jpa_json_demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * RoleInfo
 *
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RoleInfo {
    @Id
    private Long id;

    // 1: 超级管理员 2: 普通用户 3：测试角色1 ....
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<UserInfo> users;
}
