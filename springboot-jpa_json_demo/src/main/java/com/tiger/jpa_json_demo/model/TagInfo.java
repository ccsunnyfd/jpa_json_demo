package com.tiger.jpa_json_demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.swing.text.html.HTML;
import java.util.Set;

/**
 * TagInfo
 *
 * @version 1.0
 */
//@Entity
//@EntityListeners(AuditingEntityListener.class)
//@Getter
//@Setter
// 使用generator = ObjectIdGenerators.PropertyGenerator.class, property = "id"可以解决问题，但还是会返回其他子类的信息
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TagInfo {

    // 默认GenerationType.AUTO使用表自增，不推荐，请使用native方式
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "tagName", nullable = false, length = 20)
    private String tagName;

    // 多对多关系的被维护方
    // 在getArticleById返回自动序列化json格式的时候，为了避免无限循环引用多对多关系出现溢出，需要加上JsonBackReference注解，但这样被动方不能返回主动方的信息
    // 在需要双向返回child信息时，不用JsonManagedReference/JsonBackReference. 使用JsonIdentityInfo(https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion)。
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Article> articles;
}
