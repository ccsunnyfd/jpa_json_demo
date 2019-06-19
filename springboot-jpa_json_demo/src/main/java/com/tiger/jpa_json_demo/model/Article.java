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
import java.util.Set;

/**
 * Article
 *
 * @version 1.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
// 使用generator = ObjectIdGenerators.PropertyGenerator.class, property = "id"可以解决问题，但还是会返回其他子类的信息
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Article {
    // 默认GenerationType.AUTO使用表自增，不推荐，请使用native方式
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "title", nullable = false, length = 70)
    private String title;

    @Column(columnDefinition = "TEXT", name = "mdContent")
    private String mdContent;

    @Column(columnDefinition = "TEXT", name = "htmlContent")
    private String htmlContent;

    @Column(name = "summary", length = 200)
    private String summary;
    //
//    @
//    private Long cid;
//
    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo user;

    @CreatedDate
    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "publishTime")
    private Date publishTime;

    //0表示草稿箱，1表示已发表，2表示已删除'
    @Column(name = "state", nullable = false, length = 1)
    private Integer state;

    // pv: 页面点击量
    @Column(name = "pageView", length = 10)
    private Integer pageView;

    @LastModifiedDate
    @Column(name = "editTime")
    private Date editTime;

//    private String nickname;

    // 分类
    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryInfo category;

    // 标签（可以多个）
    // 多对多关系的主动维护方
    // 在getArticleById返回自动序列化json格式的时候，为了避免无限循环引用多对多关系出现溢出，需要加上JsonManagedReference注解
    // 在需要双向返回child信息时，不用JsonManagedReference/JsonBackReference. 使用JsonIdentityInfo(https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion)。
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "article_tag", inverseJoinColumns = @JoinColumn(name = "tag_id"), joinColumns = @JoinColumn(name = "article_id"))
    private Set<TagInfo> tags;

//
//    private String stateStr;
}