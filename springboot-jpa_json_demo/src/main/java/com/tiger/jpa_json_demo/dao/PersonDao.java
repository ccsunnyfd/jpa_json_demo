package com.tiger.jpa_json_demo.dao;

import com.tiger.jpa_json_demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * PersonDao
 *
 * @version 1.0
 */
public interface PersonDao extends JpaRepository<Person, Long> {
    List<Person> findByName(String name);
}
