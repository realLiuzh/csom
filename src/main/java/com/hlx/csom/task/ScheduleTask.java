//package com.hlx.csom.task;
//
//import com.hlx.csom.utils.FastjsonUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @ClassName ScheduleTask
// * @Description TODO 定时落库任务
// * @Author lzh
// * @Date 2021/7/21 16:10
// */
//
//public class ScheduleTask {
//
//    private Logger logger= LoggerFactory.getLogger(ScheduleTask.class);
//
//    private DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    //小程序总的浏览量
//    private String TOTAL_VISIT_COUNT = "TOTAL:VISIT:COUNT";
//
//    //用户点过赞的文章
//    private String USER_LIKE_ARTICLE = "USER:LIKE:ARTICLE";
//
//    //文章被点赞总数
//    private String ARTICLE_LIKE_COUNT = "ARTICLE:LIKE:COUNT";
//
//    //文章被浏览数
//    private String ARTICLE_VISIT_COUNT = "ARTICLE:VISIT:COUNT";
//
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    @Scheduled(cron = "0 0 0/1 * * ? ")
//    public void redisDataToMySQL(){
//        logger.info("time:{},开始执行Redis数据持久化到MySQL任务", LocalDateTime.now().format(formatter));
//
//        //1.更新文章总的点赞数
//        Map<String,String> articleCountMap=redisTemplate.opsForHash().entries(ARTICLE_LIKE_COUNT);
//        for (Map.Entry<String, String> entry : articleCountMap.entrySet()) {
//            String articleId = entry.getKey();
//            Set<Long> userIdSet = FastjsonUtil.deserializeToSet(entry.getValue(), Long.class);
//            synchronizeTotalLikeCount
//        }
//
//    }
//
//
//}
