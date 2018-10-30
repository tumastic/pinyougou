package cn.itcast.demo;

import org.csource.fastdfs.*;
import sun.applet.Main;

import java.io.*;

public class FastDFSDemo {

    public static void main(String[] args) throws Exception {
        // 1、 加载配置文件， 配置文件中的内容就是 tracker 服务的地址。
        ClientGlobal.init("D:\\temp\\git\\repository_pinyougou\\pinyougou_project\\itcast65\\fastDFSDemo\\src\\main\\resources\\fdfs_client.conf");
        // 2、 创建一个 TrackerClient 对象。 直接 new 一个。
        TrackerClient client = new TrackerClient();
        // 3、 使用 TrackerClient 对象创建连接， 获得一个 TrackerServer 对象。
        TrackerServer trackerServer = client.getConnection();
        // 4、 创建一个 StorageServer 的引用， 值为 null
        StorageServer storageServer = null;
        // 5、 创建一个 StorageClient 对象， 需要两个参数 TrackerServer 对象、 StorageServer的引用
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        // 6、 使用 StorageClient 对象上传图片。
        //参数一:上传文件路径  参数二:图片扩展名,扩展名不带“.”  参数三:文件信息
       String[] files = storageClient.upload_file("D:\\temp\\git\\repository_pinyougou\\pinyougou_project\\itcast65\\fastDFSDemo\\src\\main\\java\\img\\test1.jpg", "jpg", null);
        // 7、 返回数组。 包含组名和图片的路径。
        for (String file : files) {
            System.out.println(file);
        }
       //图片下载
        /*
        byte[] group1s = storageClient.download_file("group1", "M00/00/00/wKgZhVvRHPCAakC6AABNqp9rI5g407.jpg");
        File file = new File("D:\\fast\\test.jpg");
        OutputStream out = new FileOutputStream(file);
        out.write(group1s);
        out.flush();
        out.close();
        */
    }
}
