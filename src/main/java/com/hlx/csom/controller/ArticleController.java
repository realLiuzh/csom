package com.hlx.csom.controller;

import com.hlx.csom.base.ResultInfo;
import com.hlx.csom.pojo.Article;
import com.hlx.csom.service.ArticleService;
import com.hlx.csom.service.RedisService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ArticleController
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/13 18:02
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private RedisService redisService;

    /**
     * @param type
     * @param page
     * @Description //TODO 展示文章列表
     * @Date 2021/7/14 11:39
     * @Return com.hlx.csom.base.ResultInfo
     */
    @GetMapping("/list")
    public ResultInfo list(Integer type, Integer page) {
        Map<String, Object> map = articleService.list(type, page);
        return new ResultInfo(map);
    }

    /**
     * @param id
     * @Description //TODO 查看文章的详情
     * @Date 2021/7/14 11:38
     * @Return com.hlx.csom.base.ResultInfo
     */
    @GetMapping("/details")
    public ResultInfo details(Integer id) {
        Article article = articleService.details(id);
        redisService.addVisitNum(id);
        return new ResultInfo(article);
    }

    /**
     * @param key
     * @Description //TODO 全局搜索
     * @Date 2021/7/15 15:37
     * @Return com.hlx.csom.base.ResultInfo
     */
    @GetMapping("/search")
    public ResultInfo search(String key) {
        List<Article> list = articleService.search(key);
        return new ResultInfo(list);
    }

    /**
     * @param article
     * @Description //TODO 新增文章
     * @Date 2021/7/15 16:10
     * @Return com.hlx.csom.base.ResultInfo
     */
    @PostMapping("/add")
    public ResultInfo add(@RequestBody Article article) {
        articleService.add(article);
        return new ResultInfo();
    }


    /**
     * @param
     * @Description //TODO 删除文章
     * @Date 2021/7/15 16:10
     * @Return com.hlx.csom.base.ResultInfo
     */
    @DeleteMapping("/delete")
    public ResultInfo delete(Integer id) {
        articleService.delete(id);
        return new ResultInfo();
    }

    /**
     * @param article
     * @Description //TODO 修改文章
     * @Date 2021/7/15 16:27
     * @Return com.hlx.csom.base.ResultInfo
     */
    @PostMapping("/update")
    public ResultInfo update(@RequestBody Article article) {
        articleService.update(article);
        return new ResultInfo();
    }

    /**
     * @Description //TODO 点赞展示
     * @Date 2021/7/15 17:10
     * @Return com.hlx.csom.base.ResultInfo
     */
    @GetMapping("/likelist")
    public ResultInfo likeList() {
        return new ResultInfo(articleService.likeList());
    }
}
