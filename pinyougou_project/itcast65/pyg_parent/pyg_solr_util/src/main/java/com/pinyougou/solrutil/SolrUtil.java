package com.pinyougou.solrutil;


import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    public SolrTemplate solrTemplate;

    public void importItemData(){

        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");    //1-正常，2-下架，3-删除
        List<TbItem> list = itemMapper.selectByExample(example);
        for(TbItem item:list){
            System.out.println(item.getId()+" "+ item.getTitle()+ " "+item.getPrice());
           Map<String,String> specMap = JSON.parseObject(item.getSpec(), Map.class);//从数据库中提取规格json字符串转换为map
            item.setSpecMap(specMap);
        }

        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }


    public void search(){
        SimpleQuery query = new SimpleQuery("*:*");
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        List<TbItem> list = tbItems.getContent();
        for (TbItem tbItem : list) {
            System.out.println(tbItem.getTitle());
        }
    }

    public void deleAll(){
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    public static void main(String[] args) {
        //写*号导入了solr的spring配置还导入了dao的spring配置，因为要用mapper查数据库
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil=  (SolrUtil) context.getBean("solrUtil");
        //solrUtil.importItemData();
        solrUtil.search();
        //solrUtil.deleAll();;
    }
}
