package com.tiger.jpa_json_demo.dao;

import com.tiger.jpa_json_demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PersonDao
 *
 * @version 1.0
 */
public interface PersonDao extends JpaRepository<Person, Long> {
    Person findByName(String name);
}
