package com.hlx.csom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlx.csom.mapper.ArticleVisitMapper;
import com.hlx.csom.pojo.ArticleCount;
import com.hlx.csom.service.ArticleVisitService;
import org.springframework.stereotype.Service;

@Service
public class ArticleVisitServiceImpl extends ServiceImpl<ArticleVisitMapper, ArticleCount> implements ArticleVisitService {
}
