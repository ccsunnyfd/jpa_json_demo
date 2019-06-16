package com.tiger.jpa_json_demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

/**
 * UserInfo
 *
 * @version 1.0
 */
@Table(name="Userinfo")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserInfo implements UserDetails {
    // 默认GenerationType.AUTO使用表自增，不推荐，请使用native方式
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "enabled", columnDefinition = "int default 1")
    private Integer enabled;    // 1:enabled 0:disabled

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "user_role", inverseJoinColumns = @JoinColumn(name="role_id"), joinColumns = @JoinColumn(name = "user_id"))
    private Set<RoleInfo> roles;

    private String email;
    private String userface;
    private Date regTime;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleInfo role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return getEnabled() == 1;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
