package com.hlx.csom.service;


import com.hlx.csom.pojo.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService  {
    Map<String, Object> list(Integer type, Integer page);

    Article details(Integer id);

    void like(String userId,Integer articleId);

    Map<String,Object> liked(String userId) throws Exception;

    List<Article> search(String key);

    void add(Article article);

    void delete(Integer id);

    void update(Article article);

    List<Article> likeList();

    boolean isLike(String userId, Integer articleId);

    String getOpenId(String userId) throws Exception;
}
