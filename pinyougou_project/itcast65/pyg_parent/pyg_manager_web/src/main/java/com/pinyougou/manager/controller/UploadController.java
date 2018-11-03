package com.pinyougou.manager.controller;

import entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;


@RestController
public class UploadController {

    private static String service_url = "http://192.168.25.133/";

    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        try {
            String filename = file.getOriginalFilename();//全文件名称
            //截取扩展名
            String extName = filename.substring(filename.lastIndexOf(".") + 1);

            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            String fileUrl = client.uploadFile(file.getBytes(), extName);
            return new Result(true,service_url +  fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传图片失败");
        }
    }
}
