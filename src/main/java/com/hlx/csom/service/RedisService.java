package com.hlx.csom.service;

import java.util.List;

public interface RedisService {
    void likeArticle(Long articleId,String likePostId);

    void unlikeArticle(Long articleId,String likePostId);

    List<Long> getUserLikeArticleIds(String likedPostId);

    Long countArticleLike(Long articleId);


    void addVisitNum(Integer id);

    Long countArticleVisit(Integer articleId);

    Long countVisit();

    boolean checkLike(Long articleId, String likedPoseId);
}
