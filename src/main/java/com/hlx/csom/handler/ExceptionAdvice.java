package com.hlx.csom.handler;

import com.hlx.csom.base.ResultInfo;
import com.hlx.csom.exceptions.GlobalException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ExceptionAdvice
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/15 17:37
 */
@ControllerAdvice
@RestController
public class ExceptionAdvice {

    @ExceptionHandler({GlobalException.class})
    public ResultInfo handler1(GlobalException e){
        return new ResultInfo(400, e.getMsg());
    }

}
