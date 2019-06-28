package com.tiger.jpa_json_demo.service;

import com.tiger.jpa_json_demo.dao.CategoryDao;
import com.tiger.jpa_json_demo.model.CategoryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
/**
 * CategoryService
 *
 * @version 1.0
 */
@Service
public class CategoryService {
    private CategoryDao categoryDao;

    @Autowired
    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<CategoryInfo> getAllCategories() {
        return categoryDao.findAll();
    }

    @Transactional
    public Boolean deleteCategoryByIds(String cids) {
        String[] split = cids.split(",");
        for (String cid : split
        ) {
            categoryDao.deleteById(Long.valueOf(cid));
        }
        return true;
    }

    @Transactional
    public Long addOrUpdateCategory(CategoryInfo categoryInfo) {
        if (categoryInfo.getCateName() == null || "".equals(categoryInfo.getCateName())) {
            return (long) -1;
        }
        CategoryInfo retCat = categoryDao.save(categoryInfo);
        return retCat.getId();
    }

    public CategoryInfo getCategoryById(Long cid) {
        return categoryDao.getCategoryInfoById(cid);
    }
}
