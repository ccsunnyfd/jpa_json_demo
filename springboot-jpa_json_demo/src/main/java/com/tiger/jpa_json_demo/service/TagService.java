package com.tiger.jpa_json_demo.service;

import com.tiger.jpa_json_demo.dao.TagDao;
import com.tiger.jpa_json_demo.model.TagInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TagsService
 *
 * @version 1.0
 */
@Service
public class TagService {
    private TagDao tagDao;

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Transactional
    public Long saveTag(TagInfo tag) {
        //处理空串
        if (tag.getTagName() == null || "".equals(tag.getTagName())) {
            return (long)-1;
        }
        TagInfo retTag = tagDao.save(tag);
        return retTag.getId();
    }

    public TagInfo getTagById(Long tid) {
        return tagDao.getTagsById(tid);
    }
}
