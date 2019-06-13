package com.tiger.jpa_json_demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Person
 *
 * @version 1.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = true, length = 20)
    private String name;

    @Column(name = "agee", nullable = true, length = 4)
    private int age;

    @CreatedDate
    @Column(name = "gifttime")
    private Date gifttime;
}
