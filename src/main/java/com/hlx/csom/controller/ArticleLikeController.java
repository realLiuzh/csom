package com.hlx.csom.controller;

import com.hlx.csom.base.ResultInfo;
import com.hlx.csom.service.ArticleService;
import com.hlx.csom.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ArticleLikeController
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/21 8:45
 */
@RestController
@RequestMapping("/article")
public class ArticleLikeController {

    @Resource
    private RedisService redisService;

    @Resource
    private ArticleService articleService;


    /**

     * @Description //TODO 点赞或者取消点赞（暴露）
     * @Date 2021/7/21 10:30
     * @Return com.hlx.csom.base.ResultInfo
     */
    @PostMapping("/like")
    public ResultInfo like(@RequestBody Map<String, String> map) {

        Long articleId=null;
        String userId=null;

        if (map.containsKey("articleId")) {
            articleId = Long.parseLong(map.get("articleId"));
        }
        if (map.containsKey("userId")) {
            userId = map.get("userId");
        }

        if (redisService.checkLike(articleId, userId)) {
            return unlikeArticle(articleId, userId);
        } else {
            return likeArticle(articleId, userId);
        }
    }


    /**
     * @param articleId
     * @param likedPoseId
     * @Description //TODO 点赞文章
     * @Date 2021/7/21 8:52
     * @Return com.hlx.csom.base.ResultInfo
     */
    @PostMapping("/{articleId}/{likedPoseId}")
    public ResultInfo likeArticle(@PathVariable("articleId") Long articleId,
                                  @PathVariable("likedPoseId") String likedPoseId) {
        redisService.likeArticle(articleId, likedPoseId);
        return new ResultInfo(200, "点赞成功");
    }


    /**
     * @param articleId
     * @param likedPoseId
     * @Description //TODO 取消点赞成功
     * @Date 2021/7/21 8:55
     * @Return com.hlx.csom.base.ResultInfo
     */
    @DeleteMapping("/{articleId}/{likedPoseId}")
    public ResultInfo unlikeArticle(@PathVariable("articleId") Long articleId,
                                    @PathVariable("likedPoseId") String likedPoseId) {
        redisService.unlikeArticle(articleId, likedPoseId);
        return new ResultInfo(200, "取消点赞成功");
    }

    /**
     * @param likedPoseId
     * @Description //TODO 获取用户点赞的文章
     * @Date 2021/7/21 8:57
     * @Return com.hlx.csom.base.ResultInfo
     */
    @GetMapping("/user/like")
    public ResultInfo getUserLikeArticle(String likedPoseId) throws Exception {
        //根据code获取open_id
        String openId = articleService.getOpenId(likedPoseId);
        //List<Long> list = redisService.getUserLikeArticleIds(openId);

        Map<String, Object> map = new HashMap<>();
        map.put("openId", openId);
        //map.put("list",list);
        return new ResultInfo(map);
    }


    /**
     * @param userId
     * @param articleId
     * @Description //TODO userId是否点赞过articleId
     * @Date 2021/7/21 12:22
     * @Return com.hlx.csom.base.ResultInfo
     */
    @GetMapping("/islike")
    public ResultInfo isLike(String userId, Integer articleId) throws Exception {
        List<Long> list = redisService.getUserLikeArticleIds(userId);
        boolean flag = list.contains(Long.parseLong(String.valueOf(articleId)));
        return new ResultInfo(flag);
    }


    /**
     * @param articleId
     * @Description //TODO 统计某篇文章总点赞数（暴露）
     * @Date 2021/7/21 9:00
     * @Return com.hlx.csom.base.ResultInfo
     */
    @GetMapping("/total/like/{articleId}")
    public ResultInfo countArticleLike(@PathVariable("articleId") Long articleId) {
        return new ResultInfo(redisService.countArticleLike(articleId));
    }

    /**
     * @param articleId
     * @Description //TODO 统计某篇文章总浏览数 （暴露）
     * @Date 2021/7/21 10:08
     * @Return com.hlx.csom.base.ResultInfo
     */
    @GetMapping("/total/visit/{articleId}")
    public ResultInfo countArticleVisit(@PathVariable("articleId") Integer articleId) {
        return new ResultInfo(redisService.countArticleVisit(articleId));
    }

    @GetMapping("/total/{articleId}")
    public ResultInfo count(@PathVariable("articleId") Long articleId){
        Long like = redisService.countArticleLike(articleId);
        Long visit = redisService.countArticleVisit(Integer.parseInt(String.valueOf(articleId)));
        Map<String,Long> map=new HashMap<>();
        map.put("likeNum",like);
        map.put("visitNum",visit);
        return new ResultInfo(map);
    }

    /**
     * @param
     * @Description //TODO 小程序总的浏览数 （暴露）
     * @Date 2021/7/21 10:33
     * @Return com.hlx.csom.base.ResultInfo
     */
    @GetMapping("/total/visit")
    public ResultInfo countVisit() {
        return new ResultInfo(redisService.countVisit());
    }


}
