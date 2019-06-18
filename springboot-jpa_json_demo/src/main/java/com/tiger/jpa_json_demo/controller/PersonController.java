package com.tiger.jpa_json_demo.controller;

import com.tiger.jpa_json_demo.dao.PersonDao;
import com.tiger.jpa_json_demo.model.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PersonController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/person")
@Api(value = "person信息的增删改查")
public class PersonController {
    @Autowired
    private PersonDao personDao;

    // 如果使用@RequestBody，那么会自动将从前端传过来的json格式的参数封装为java bean对象，可以解决Postman在Post body中发送json格式的数据接收不到的问题
    // 如果不使用@RequestBody，那么vue-resource方式的前端脚本在跨域请求时需要加上{ emulateJSON: true }，使用了@RequestBody则要注释掉这个JSON模拟，否则浏览器会报415错误
    /**
     * 添加指定person
     * api: localhost:8080/api/person/addPerson
     * @param person
     */
    @PostMapping(path = "addPerson")
    @ApiOperation(value="添加指定person")
    public void addPerson(@RequestBody Person person) {
        personDao.save(person);
    }

    /**
     * 删除指定person
     * api: localhost:8080/api/person/deletePerson
     * @param person
     */
    @DeleteMapping(path = "deletePerson")
    @ApiOperation(value="删除指定person")
    public void deletePerson(@RequestBody Person person) {
        personDao.delete(person);
    }

    /**
     * 根据名字查询person信息
     * api: localhost:8080/api/person/getPerson?name=xxx
     * @param name
     * @return
     */
    @GetMapping("getPerson")
    @ApiOperation(value = "根据名字查询person信息")
    public List<Person> getPerson(@RequestParam("name") String name) {
        return personDao.findByName(name);
    }
}
