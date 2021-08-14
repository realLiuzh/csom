package com.hlx.csom.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface FileService {
    String upload(MultipartFile file);

    void download(String name, HttpServletResponse response) throws UnsupportedEncodingException;

    Resource loadFileAsResource(String fileName);

    List<String> list();
}
