package com.hlx.csom.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hlx.csom.mapper.ArticleMapper;
import com.hlx.csom.mapper.ArticlePhotoNameMapper;
import com.hlx.csom.mapper.UserLikeArticleMapper;
import com.hlx.csom.pojo.Article;
import com.hlx.csom.pojo.ArticlePhoto;
import com.hlx.csom.pojo.UserLikeArticle;
import com.hlx.csom.pojo.Wx;
import com.hlx.csom.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName ArticleServiceImpl
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/13 18:05
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    //private static final String APPID = "wx88f0a11a719b1fa3";
    private static final String APPID = "wxb8cfba61db1c220e";
    private static final String APPSECRET = "d5254280461ca4b99023dc72edb762a4";
    //private static final String APPSECRET = "da8d5a6d89d940c6595343fd8fa53433";

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserLikeArticleMapper userLikeArticleMapper;

    @Resource
    private ArticlePhotoNameMapper articlePhotoNameMapper;


    @Override
    public Map<String, Object> list(Integer type, Integer page) {

        Page<Article> p = new Page<>(page, 6);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("id", "title", "author", "create_date", "type", "update_date")
                .eq("type", type)
                .eq("is_valid", 1)
                .orderByDesc("update_date");
        articleMapper.selectPage(p, queryWrapper);


        Map<String, Object> map = new HashMap<>();
        map.put("currPage", p.getCurrent());
        map.put("totalPage", p.getPages());
        map.put("list", p.getRecords());
        return map;
    }

    @Override
    public Article details(Integer id) {
        Article article = articleMapper.selectById(id);
       /* QueryWrapper<ArticlePhoto> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("photo_name")
                .eq("article_id", id);
        List<ArticlePhoto> articlePhotos = articlePhotoNameMapper.selectList(queryWrapper);
        List<Map<String,String>> photoNames = new ArrayList<>();
        for (ArticlePhoto articlePhoto : articlePhotos) {
            Map<String,String> map=new HashMap<>();
            map.put("name",articlePhoto.getPhotoName());
            map.put("url","http://47.96.86.132:8080/csom/file/download?name="+articlePhoto.getPhotoName());
            photoNames.add(map);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("id",article.getId());
        map.put("type",article.getType());
        map.put("author",article.getAuthor());
        map.put("edit",article.getEdit());
        map.put("check",article.getCheck());
        map.put("finalCheck",article.getFinalCheck());
        map.put("title",article.getTitle());
        map.put("content",article.getContent());
        map.put("photoName",photoNames);*/

        article.setCurrent("");
        article.setUrls(getUrls(article.getContent()));
        return article;
    }

    private List<String> getUrls(String content) {
        Pattern p= Pattern.compile("http[s]?://(?:[a-zA-Z]|[0-9]|[\\u4e00-\\u9fa5]|[$-_@.&+]|[!*\\(\\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+");
        Matcher m=p.matcher(content);
        List<String> list=new ArrayList<>();
        while(m.find())
        {
            list.add(m.group());
        }
        return list;
    }


    @Override
    public void like(String userId, Integer articleId) {
        QueryWrapper<UserLikeArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id", userId)
                .eq("article_id", articleId);
        UserLikeArticle userLikeArticle = userLikeArticleMapper.selectOne(queryWrapper);
        QueryWrapper<Article> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2
                .eq("id", articleId);
        Article article = articleMapper.selectOne(queryWrapper2);

        if (userLikeArticle != null) {
            //取消赞
            cancelLike(queryWrapper);
            article.setLikeNum(article.getLikeNum() - 1);
        } else {
            //点赞
            addLike(userId, articleId);
            article.setLikeNum(article.getLikeNum() + 1);
        }

        articleMapper.updateById(article);

    }

    /**
     * @param userId
     * @Description //TODO 查看用户点过赞的文章
     * @Date 2021/7/16 18:08
     * @Return java.util.List<java.lang.Integer>
     */
    @Override
    public Map<String, Object> liked(String userId) throws Exception {
        String open_id = getOpenId(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("openId", open_id);
        return map;
    }

    public String getOpenId(String userId) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APPID + "&secret=" + APPSECRET + "&js_code=" + userId + "&grant_type=authorization_code";
        URL obj = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //打印结果
        System.out.println(response.toString());
        Wx parse = JSON.parseObject(response.toString(), Wx.class);
        System.out.println(parse.getOpenid());

        return parse.getOpenid();

    }

    @Override
    public List<Article> search(String key) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .like("title", key);
        List<Article> list = articleMapper.selectList(queryWrapper);
        return list;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void add(Article article) {
        //1.设置默认值
        article = setDefaultValue(article);
        //2.插入数据
        articleMapper.insert(article);
        //3.插入图片
        /*List<String> photoName = article.getPhotoName();
        for (String s : photoName) {
            articlePhotoNameMapper.insert(new ArticlePhoto(article.getId(), s));
        }*/
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Integer id) {
        UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("is_valid", 0);
        articleMapper.update(null, updateWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Article article) {
        article.setUpdateDate(new Date());
        articleMapper.updateById(article);
        /*QueryWrapper<ArticlePhoto> queryWrapper = new QueryWrapper();
        queryWrapper.eq("article_id", article.getId());
        articlePhotoNameMapper.delete(queryWrapper);*/

        //插入图片
        /*List<String> photoName = article.getPhotoName();
        for (String s : photoName) {
            articlePhotoNameMapper.insert(new ArticlePhoto(article.getId(), s));
        }*/
    }

    @Override
    public List<Article> likeList() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("id", "title", "author", "type", "update_date", "create_date", "like_num")
                .eq("is_valid", 1)
                .orderByDesc("like_num");
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public boolean isLike(String userId, Integer articleId) {
        QueryWrapper<UserLikeArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("article_id", articleId);
        UserLikeArticle userLikeArticle = userLikeArticleMapper.selectOne(queryWrapper);

        if (userLikeArticle == null) return false;
        return true;
    }

    private Article setDefaultValue(Article article) {
        article.setCreateDate(new Date());
        article.setUpdateDate(new Date());
        article.setLikeNum(0);
        article.setIsValid(1);
        return article;
    }

    private void addLike(String userId, Integer articleId) {
        UserLikeArticle userLikeArticle = new UserLikeArticle(userId, articleId);
        userLikeArticleMapper.insert(userLikeArticle);
    }

    private void cancelLike(QueryWrapper<UserLikeArticle> queryWrapper) {
        userLikeArticleMapper.delete(queryWrapper);
    }


}
