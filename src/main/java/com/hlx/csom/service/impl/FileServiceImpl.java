package com.hlx.csom.service.impl;

import com.hlx.csom.exceptions.GlobalException;
import com.hlx.csom.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FileServiceImpl
 * @Description TODO
 * @Author lzh
 * @Date 2021/7/15 17:18
 */

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new GlobalException("未选择需要上传的文件");
        }

        String filePath = new File(File.separator + "file" + File.separator).getAbsolutePath();
        File fileUpload = new File(filePath);
        if (!fileUpload.exists()) {
            fileUpload.mkdirs();
        }

        //如果是图片 jpg png等格式的需要改变其文件名
        String name=getFileName(file);

        fileUpload = new File(filePath, name);
        //fileUpload = new File(filePath, file.getOriginalFilename());
        if (fileUpload.exists()) {
            return name;
        }

        try {
            file.transferTo(fileUpload);

        } catch (IOException e) {
            throw new GlobalException("上传文件到服务器失败:" + e.toString());
        }
        return name;

    }

    private String getFileName(MultipartFile file) {
        String contentType = file.getContentType();
        //如果不是图片就返回原来的名字
        if(!contentType.contains("image")){
            return file.getOriginalFilename();
        }
        //如果是图片
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return  simpleDateFormat.format(new Date())+file.getOriginalFilename();
    }

    @Override
    public void download(String name, HttpServletResponse response) throws UnsupportedEncodingException {
        String fileName = name;// 设置文件名
        if (fileName != null) {
            //设置文件路径
            String realPath = File.separator + "file" + File.separator;
            File file = new File(realPath, fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));// 设置文件名和编码格式
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


//    @Override
//    public void download(String name, HttpServletResponse response) {
//
////        File file = new File("file");
//        File file = new File("/file/");
//        if (!file.exists()) {
//            throw new GlobalException("文件不存在");
//        }
//
//        try {
//            name = new String(name.getBytes(), "ISO-8859-1");
//        }catch (Exception e){
//            throw new GlobalException("文件名错误,文件名建议使用英文");
//        }
//
//        response.setContentType("application/force-download");
//        response.addHeader("Content-Disposition", "attachment;fileName=" + name);
//
//        byte[] buffer = new byte[1024];
//        try (FileInputStream fis = new FileInputStream(file);
//             BufferedInputStream bis=new BufferedInputStream(fis);
//        ) {
//            OutputStream os=response.getOutputStream();
//            int i=bis.read(buffer);
//            while (i!=-1){
//                os.write(buffer,0,i);
//                i=bis.read(buffer);
//            }
//
//        }catch (Exception e){
//           throw new GlobalException(e.toString());
//        }
//    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        return null;
    }

    /* @Override
     public List<String> list() {
         File file = new File(File.separator+"file" + File.separator);
         String[] list = file.list();
         List<String> list2 = Arrays.asList(list);
         return list2;
     }*/
    @Override
    public List<String> list() {
        File file = new File(File.separator + "file" + File.separator);
        //String[] list = file.list();
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                File file1 = new File(dir, name);
                return file1.isFile() && !file1.getName().endsWith(".jpg") && !file1.getName().endsWith(".png");
            }
        });
        List<String> list = new ArrayList<>();
        for (File file1 : files) {
            list.add(file1.getName());
        }
        return list;
    }
}
