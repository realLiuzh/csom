package com.hlx.csom.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hlx.csom.enums.TypeEnum;
import com.hlx.csom.pojo.ArticlePhoto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @ClassName Article
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/13 18:07
 */
@Setter
@Getter
public class ArticleVO {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private TypeEnum type;

    private String title;

    private String author;

    private String content;

    @TableField(exist = false)
    private List<String> photoName;

    private Integer likeNum;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateDate;

    private Integer isValid;

}
