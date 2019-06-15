package com.tiger.jpa_json_demo.controller;

import com.tiger.jpa_json_demo.model.CategoryInfo;
import com.tiger.jpa_json_demo.model.RespBean;
import com.tiger.jpa_json_demo.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * CategoryController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/admin/category")
@Api(value = "category信息的增删改查")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 查询所有的种类
     * api: localhost:8080/api/admin/category/all
     * @return List<CategoryInfo>
     */
    @GetMapping(path = "all")
    @ApiOperation(value = "查询所有的种类")
    public List<CategoryInfo> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // 如果使用@RequestBody，那么会自动将从前端传过来的json格式的参数封装为java bean对象，可以解决Postman在Post body中发送json格式的数据接收不到的问题
    // 如果不使用@RequestBody，那么vue-resource方式的前端脚本在跨域请求时需要加上{ emulateJSON: true }，使用了@RequestBody则要注释掉这个JSON模拟，否则浏览器会报415错误

    /**
     * 添加指定category
     * api: localhost:8080/api/admin/category/addCategory
     *
     * @param categoryInfo category
     * @return RespBean
     */
    @PostMapping(path = "addCategory")
    @ApiOperation(value = "添加指定category")
    public RespBean addCategory(@RequestBody CategoryInfo categoryInfo) {
        Long result = categoryService.addOrUpdateCategory(categoryInfo);
        if (result != null) {
            return new RespBean("success", result + "");
        } else {
            return new RespBean("error", "Category保存失败!");
        }
    }

    /**
     * 更新指定category
     * api: localhost:8080/api/admin/category/updateCategory
     *
     * @param categoryInfo category
     * @return RespBean
     */
    @PostMapping(path = "updateCategory")
    @ApiOperation(value = "更新指定category")
    public RespBean updateCategory(@RequestBody CategoryInfo categoryInfo) {
        Long result = categoryService.addOrUpdateCategory(categoryInfo);
        if (result != null) {
            return new RespBean("success", result + "");
        } else {
            return new RespBean("error", "Category更新失败!");
        }
    }

    /**
     * 根据id查询category信息
     * api: localhost:8080/api/admin/category/getCategoryById?cid=xxx
     *
     * @param cid cid
     * @return categoryInfo
     */
    @GetMapping("getCategoryById")
    @ApiOperation(value = "根据cid查询category信息")
    public CategoryInfo getCategoryById(@RequestParam("cid") Long cid) {
        return categoryService.getCategoryById(cid);
    }

    /**
     * 根据cids删除指定的Category
     * api: localhost:8080/api/admin/category/deleteCategoryByIds
     * @param cids List<Long> cids
     * @return RespBean
     */
    @DeleteMapping("deleteCategoryByIds")
    @ApiOperation(value = "根据cids删除指定的Category")
    public RespBean deleteCategoryById(@RequestParam("cids") List<Long> cids) {
        Boolean result = categoryService.deleteCategoryByIds(cids);
        if (result) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }
}
