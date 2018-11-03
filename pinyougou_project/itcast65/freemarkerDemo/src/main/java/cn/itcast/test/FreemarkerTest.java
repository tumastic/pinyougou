package cn.itcast.test;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FreemarkerTest {

    public static void main(String[] args) throws IOException, TemplateException {
        // 1.创建配置类Configuration
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 2.设置模板所在的目录，注意是文件所在目录不是文件全路径
        configuration.setDirectoryForTemplateLoading(new File("D:\\temp\\git\\repository_pinyougou\\pinyougou_project\\itcast65\\freemarkerDemo\\src\\main\\resources"));
        // 3.设置字符集
        configuration.setDefaultEncoding("utf-8");
        // 4.加载模板
        Template template = configuration.getTemplate("index.ftl");
        // 5.准备导出数据
        Map map = new HashMap();
        map.put("name", "测试网易阴阳师");
        map.put("success","false1");


        List goodsList=new ArrayList();
        Map goods1=new HashMap();
        goods1.put("name", "苹果");
        goods1.put("price", 5.8);
        Map goods2=new HashMap();
        goods2.put("name", "香蕉");
        goods2.put("price", 2.5);
        Map goods3=new HashMap();
        goods3.put("name", "橘子");
        goods3.put("price", 3.2);
        goodsList.add(goods1);
        goodsList.add(goods2);
        goodsList.add(goods3);
        map.put("goodsList", goodsList);

        map.put("price", 9999999);

        map.put("timenow", new Date());
        // 6.创建Writer对象
        FileWriter writer = new FileWriter(new File("D:\\temp\\git\\repository_pinyougou\\pinyougou_project\\itcast65\\freemarkerDemo\\src\\main\\web\\index.html"));
        // 7.通过template.process进行文件的生成
        template.process(map, writer);
        // 8.关闭Writer对象
        writer.close();

        System.out.println("==写出完成==");
    }
}
