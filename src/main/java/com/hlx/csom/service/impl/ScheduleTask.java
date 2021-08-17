package com.hlx.csom.service.impl;

import com.hlx.csom.mapper.ArticleLikeMapper;
import com.hlx.csom.pojo.ArticleCount;
import com.hlx.csom.pojo.ArticleLike;
import com.hlx.csom.service.ArticleLikeService;
import com.hlx.csom.service.ArticleVisitService;
import com.hlx.csom.utils.FastjsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

import static com.hlx.csom.service.impl.RedisServiceImpl.ARTICLE_LIKE_COUNT;
import static com.hlx.csom.service.impl.RedisServiceImpl.ARTICLE_VISIT_COUNT;

@Service
public class ScheduleTask {

    private Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ArticleLikeService articleLikeService;

    @Resource
    private ArticleVisitService articleVisitService;


    //    @Scheduled(fixedDelay = 1000 * 10)
    //每小时执行一次定时任务
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void redisDataToMysql() {
        logger.info("time:{},开始执行Redis数据持久化到Mysql任务", LocalDateTime.now().format(formatter));
        //更新文章点赞数
        saveArticleHashData(ARTICLE_LIKE_COUNT);
        //更新文章浏览数
        saveArticleHashData(ARTICLE_VISIT_COUNT);

        //总浏览数可以根据文章浏览数求和得到

        //用户更新数就不保存了

        logger.info("time:{},结束执行Redis数据持久化到Mysql任务", LocalDateTime.now().format(formatter));
    }

    private void saveArticleHashData(String str) {
        Map<String, String> articleCountMap1 = redisTemplate.opsForHash().entries(str);
        for (Map.Entry<String, String> entry : articleCountMap1.entrySet()) {
            if (ARTICLE_LIKE_COUNT.equals(str))
                synchronizeArticleLikeNumber(new ArticleLike(entry.getKey(), entry.getValue()));
            if (ARTICLE_VISIT_COUNT.equals(str))
                synchronizeArticleVisitNumber(new ArticleCount(entry.getKey(), entry.getValue()));
        }
    }

    private void synchronizeArticleVisitNumber(ArticleCount articleCount) {
        articleVisitService.saveOrUpdate(articleCount);
    }


    private void synchronizeArticleLikeNumber(ArticleLike articleLike) {
        articleLikeService.saveOrUpdate(articleLike);
    }


}
