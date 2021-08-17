package com.hlx.csom.service.impl;

import com.hlx.csom.exceptions.GlobalException;
import com.hlx.csom.service.RedisService;
import com.hlx.csom.utils.FastjsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName RedisServiceImpl
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/20 21:59
 */

@Service
public class RedisServiceImpl implements RedisService {
    Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    //小程序总的浏览量
    public static final String TOTAL_VISIT_COUNT = "TOTAL:VISIT:COUNT";

    //用户点过赞的文章
    public static final String USER_LIKE_ARTICLE = "USER:LIKE:ARTICLE";

    //文章被点赞总数
    public static final String ARTICLE_LIKE_COUNT = "ARTICLE:LIKE:COUNT";

    //文章被浏览数
    public static final String ARTICLE_VISIT_COUNT = "ARTICLE:VISIT:COUNT";


    @Resource
    private RedisTemplate redisTemplate;


    /**
     * @param
     * @Description //TODO 指定序列化方式
     * @Date 2021/7/20 22:12
     * @Return void
     */
    @PostConstruct
    public void init() {
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
    }

    /**
     * @param articleId
     * @param likePostId
     * @Description //TODO 用户点赞某篇文章
     * @Date 2021/7/20 22:15
     * @Return void
     */
    public void likeArticle(Long articleId, String likePostId) {
        validateParam(articleId, likePostId);

        logger.info("点赞数据存入redis开始,articleId:{},likedPostId:{}", articleId, likePostId);

        //只有未点赞的用户才可以进行点赞
        likeArticleLogicValidate(articleId, likePostId);

        //文章被点赞总数+1
        redisTemplate.opsForHash().increment(ARTICLE_LIKE_COUNT, String.valueOf(articleId), 1);
        //文章被浏览数+1
//        redisTemplate.opsForHash().increment(ARTICLE_VISIT_COUNT,String.valueOf(articleId),1);

        synchronized (this) {
            //用户喜欢的文章+1
            String userLikeResult = (String) redisTemplate.opsForHash().get(USER_LIKE_ARTICLE, String.valueOf(likePostId));
            Set<Long> articleIdSet = userLikeResult == null ? new HashSet<>() : FastjsonUtil.deserializeToSet(userLikeResult, Long.class);
            articleIdSet.add(articleId);
            redisTemplate.opsForHash().put(USER_LIKE_ARTICLE, String.valueOf(likePostId), FastjsonUtil.serialize(articleIdSet));
        }
    }

    /**
     * @param articleId
     * @param likePostId
     * @Description //TODO 点赞逻辑检验
     * @Date 2021/7/20 22:45
     * @Return void
     */
    private void likeArticleLogicValidate(Long articleId, String likePostId) {
        Set<Long> articleIdSet = FastjsonUtil.deserializeToSet(
                (String) redisTemplate.opsForHash().get(USER_LIKE_ARTICLE, String.valueOf(likePostId)), Long.class
        );
        if (articleIdSet == null) {
            return;
        }
        if (articleIdSet.contains(articleId)) {
            logger.error("改文章已被当前用户点赞,重复点赞,articleId:{},likePostId:{}", articleId, likePostId);
            throw new GlobalException(401, "改文章已被当前用户点赞,重复点赞");
        }
    }

    /**
     * @param articleId
     * @param likePostId
     * @Description //TODO 取消点赞
     * @Date 2021/7/20 22:50
     * @Return void
     */
    public void unlikeArticle(Long articleId, String likePostId) {
        validateParam(articleId, likePostId);

        logger.info("取消点赞数据存入redis开始,articleId:{},likedUserId:{}", articleId, likePostId);

//        redisTemplate.opsForHash().increment(ARTICLE_VISIT_COUNT,String.valueOf(articleId),1);

        synchronized (this) {
            //用户点赞数-1
            unlikeArticleLogicValidate(articleId, likePostId);
            Long totalLikeCount = Long.parseLong((String) redisTemplate.opsForHash().get(ARTICLE_LIKE_COUNT, String.valueOf(articleId)));
            redisTemplate.opsForHash().put(ARTICLE_LIKE_COUNT, String.valueOf(articleId), String.valueOf(--totalLikeCount));

            //用户喜爱的文章-1
            String userLikeResult = (String) redisTemplate.opsForHash().get(USER_LIKE_ARTICLE, String.valueOf(likePostId));
            Set<Long> articleIdSet = FastjsonUtil.deserializeToSet(userLikeResult, Long.class);
            articleIdSet.remove(articleId);
            redisTemplate.opsForHash().put(USER_LIKE_ARTICLE, String.valueOf(likePostId), FastjsonUtil.serialize(articleIdSet));
        }


    }

    /**
     * @param articleId
     * @param likePostId
     * @Description //TODO 取消点赞的逻辑检验
     * @Date 2021/7/20 23:35
     * @Return void
     */
    private void unlikeArticleLogicValidate(Long articleId, String likePostId) {
        Set<Long> articleIdSet = FastjsonUtil.deserializeToSet((String) redisTemplate.opsForHash().get(USER_LIKE_ARTICLE, String.valueOf(likePostId)), Long.class);
        if (articleIdSet == null) {
            return;
        }
        if (!articleIdSet.contains(articleId)) {
            logger.error("该文章未被当前用户点赞，不可以进行取消点赞操作 articleId:{},likePostId:{}", articleId, likePostId);
            throw new GlobalException(401, "该文章未被当前用户点赞，不可以进行取消点赞操作");
        }


    }


    /**
     * @param likedPostId
     * @Description //TODO 获取用户点赞过的文章
     * @Date 2021/7/20 23:42
     * @Return java.util.List<java.lang.Long>
     */
    public List<Long> getUserLikeArticleIds(String likedPostId) {
        validateParam(likedPostId);
        String userLikeResult = (String) redisTemplate.opsForHash().get(USER_LIKE_ARTICLE, likedPostId);
        Set<Long> articleIdSet = FastjsonUtil.deserializeToSet(userLikeResult, Long.class);
        if (articleIdSet == null) {
            return new ArrayList<>();
        }
        return articleIdSet.stream().collect(Collectors.toList());
    }

    /**
     * @param articleId
     * @Description //TODO 统计某篇文章总的点赞数
     * @Date 2021/7/20 23:45
     * @Return java.lang.Long
     */
    public synchronized Long countArticleLike(Long articleId) {
        validateParam(articleId);
        String s = (String) redisTemplate.opsForHash().get(ARTICLE_LIKE_COUNT, String.valueOf(articleId));
        return s == null ? 0L : Long.parseLong(s);
    }

    /**
     * @Description //TODO 统计某篇文章总的浏览量
     * @Date 2021/7/21 10:06
     * @param articleId
     * @Return java.lang.Long
     */
    @Override
    public synchronized Long countArticleVisit(Integer articleId) {
        validateParam(articleId);
        String s = (String) redisTemplate.opsForHash().get(ARTICLE_VISIT_COUNT, String.valueOf(articleId));
        return s == null ? 0L : Long.parseLong(s);
    }

    /**
     * @Description //TODO 统计小程序总的浏览量
     * @Date 2021/7/21 10:24
     * @param
     * @Return java.lang.Long
     */
    @Override
    public Long countVisit() {
        String s = (String) redisTemplate.opsForValue().get(TOTAL_VISIT_COUNT);
        return s == null ? 0L : Long.parseLong(s);
    }

    /**
     * @Description //TODO likedPoseId是否对articleId点过赞
     * @Date 2021/7/21 10:29
     * @param articleId
     * @param likedPoseId
     * @Return boolean
     */
    @Override
    public boolean checkLike(Long articleId, String likedPoseId) {
        List<Long> list = getUserLikeArticleIds(likedPoseId);
        return list.contains(articleId);
    }

    /**
     * @Description //TODO 增加浏览量
     * @Date 2021/7/21 9:54
     * @param id
     * @Return void
     */
    @Override
    public void addVisitNum(Integer id) {
        validateParam(id);
        //增加小程序的浏览量
        redisTemplate.opsForValue().increment(TOTAL_VISIT_COUNT,1);
        //增加文章的浏览量
        redisTemplate.opsForHash().increment(ARTICLE_VISIT_COUNT,String.valueOf(id),1);
    }


    /**
     * @param params
     * @Description //TODO 入参验证
     * @Date 2021/7/20 22:42
     * @Return void
     */
    private void validateParam(Object... params) {
        for (Object param : params) {
            if (null == param) {
                logger.error("入参存在null值");
                throw new GlobalException(401, "入参存在null值");
            }
        }
    }


}
