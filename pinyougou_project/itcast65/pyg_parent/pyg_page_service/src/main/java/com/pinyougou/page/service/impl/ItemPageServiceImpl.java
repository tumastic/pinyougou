package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {
    @Value("${pagedir}")
    private String pagedir;

    @Autowired
    private FreeMarkerConfigurer configurer;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public boolean createItemHtml(Long goodsId) {
        try {
            //configurer已经配置了模版所在路径在配置文件的bean上
            Configuration configuration = configurer.getConfiguration();
           // configuration.setDirectoryForTemplateLoading(new File("D:\\temp\\git\\repository_pinyougou\\pinyougou_project\\itcast65\\pyg_parent\\pyg_page_service\\src\\main\\webapp\\WEB-INF\\ftl"));
            Template template = configuration.getTemplate("item.ftl");


            //准备模版数据
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);

            Map resultMap = new HashMap();
            //商品和商品详情
            resultMap.put("goods", goods);
            resultMap.put("goodsDesc", goodsDesc);

            String itemCat1Name = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2Name = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3Name = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();

            //商品分类名称
            resultMap.put("itemCat1Name", itemCat1Name);
            resultMap.put("itemCat2Name", itemCat2Name);
            resultMap.put("itemCat3Name", itemCat3Name);


            //获取该商品下的所有sku的列表信息
            TbItemExample example = new TbItemExample();
            example.createCriteria().andGoodsIdEqualTo(goodsId);
            example.setOrderByClause("is_default desc"); //按照是否默认进行倒序查询，为1的就是第0个元素
            List<TbItem> itemList = itemMapper.selectByExample(example);
            resultMap.put("itemList", itemList);

            //要写出文件的所在路径
            FileWriter fileWriter = new FileWriter(new File(pagedir+goodsId+".html"));
            template.process(resultMap, fileWriter);
            fileWriter.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
