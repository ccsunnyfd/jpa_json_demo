package com.tiger.jpa_json_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataStatisticsComponent {
    private ArticleService articleService;

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    // 每天执行一次，统计PV
    @Scheduled(cron="1 0 0 * * ?")  //每天0点0分1秒执行
    public void pvStatisticsPerDay() {
        articleService.pvStatisticsPerDay();
    }
}
