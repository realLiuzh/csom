package com.hlx.csom.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Set;

/**
 * @ClassName FastjsonUtil
 * @Description TODO 序列化和反序列化的工具类
 * @Author lzh
 * @Date 2021/7/20 22:35
 */

public class FastjsonUtil {

    /**
     * @Description //TODO 序列化
     * @Date 2021/7/20 22:36
     * @param object 序列化对象
     * @Return java.lang.String
     */
    public static String serialize(Object object){
        return JSON.toJSONString(object);
    }

    /**
     * @Description //TODO Set反序列化
     * @Date 2021/7/20 22:38
     * @param jsonStr 序列化结果
     * @param clazz 泛型class类
     * @Return java.util.Set<T>
     */
    public static <T> Set<T> deserializeToSet(String jsonStr, Class<T> clazz){
        return JSON.parseObject(jsonStr,new TypeReference<Set<T>>(clazz){});
    }

}
