package com.tiger.jpa_json_demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Person
 *
 * @version 1.0
 */
@Entity
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
}
