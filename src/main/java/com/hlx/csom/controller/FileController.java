package com.hlx.csom.controller;

import com.hlx.csom.base.ResultInfo;
import com.hlx.csom.pojo.Photo;
import com.hlx.csom.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FileController
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/15 17:12
 */
@RestController

@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;


    /**
     * @param file
     * @Description //TODO 上传文章
     * @Date 2021/7/15 18:31
     * @Return com.hlx.csom.base.ResultInfo
     */
    @PostMapping("/upload")
    public ResultInfo upLoad(@RequestParam("file") MultipartFile file) {
        System.out.println("upload......................");
        String name = fileService.upload(file);
        return new ResultInfo("![](http://47.96.86.132:8080/csom/file/download?name=" + name + ")");
    }

    @GetMapping("/delete")
    public ResultInfo deleteFile(@RequestBody Map<String, String> map) {
        String name = map.get("name");
        if (StringUtils.isBlank(name)) {
            return new ResultInfo(300, "请输入文件名");
        }
        String filePath = new File(File.separator + "file" + File.separator).getAbsolutePath();
        File fileUpload = new File(filePath);
        fileUpload = new File(filePath, name);
        return fileUpload.delete() ? new ResultInfo("文件删除成功!") : new ResultInfo(300,"文件删除失败!");
    }


    /**
     * @param file
     * @Description //TODO 上传文章
     * @Date 2021/7/15 18:31
     * @Return com.hlx.csom.base.ResultInfo
     */
    @PostMapping("/uploadd")
    public Map<String, Object> upLoadd(@RequestParam("file") MultipartFile file) {
        System.out.println("uploadd......................");
        fileService.upload(file);
        Map<String, Object> map = new HashMap<>();
        map.put("errno", 0);
        List<Photo> list = new ArrayList<Photo>();
        list.add(new Photo("https://i.loli.net/2021/04/02/dhCOmJvDjVltex4.png", "test", "https://i.loli.net/2021/04/02/dhCOmJvDjVltex4.png"));
        map.put("data", list);
        return map;
    }


    /**
     * @param name
     * @param response
     * @Description //TODO 下载文章
     * @Date 2021/7/15 18:31
     * @Return void
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws UnsupportedEncodingException {
        fileService.download(name, response);
    }

    /**
     * @param
     * @Description //TODO 列出文章列表
     * @Date 2021/7/15 18:31
     * @Return com.hlx.csom.base.ResultInfo
     */
    @GetMapping("/list")
    public ResultInfo list() {
        System.out.println("listFile......................");
        List<String> list = fileService.list();
        return new ResultInfo(list);
    }

}
