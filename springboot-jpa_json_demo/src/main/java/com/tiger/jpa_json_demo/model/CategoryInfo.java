package com.tiger.jpa_json_demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * CategoryInfo
 *
 * @version 1.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
// 使用generator = ObjectIdGenerators.PropertyGenerator.class, property = "id"可以解决问题，但还是会返回其他子类的信息
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CategoryInfo {
    // 默认GenerationType.AUTO使用表自增，不推荐，请使用native方式
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "cateName", nullable = false, length = 20)
    private String cateName;

    @LastModifiedDate
    @Column(name = "modifiedTime")
    private Date modifiedTime;
}