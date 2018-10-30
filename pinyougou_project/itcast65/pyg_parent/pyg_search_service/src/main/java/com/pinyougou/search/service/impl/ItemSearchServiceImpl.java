package com.pinyougou.search.service.impl;



import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map search(Map searchMap) {

        String keywords = (String) searchMap.get("keywords");//先获取到页面传入的搜索关键字
        System.out.println("搜索的关键字是："+keywords);


        /*SimpleQuery query = new SimpleQuery("*:*");
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        List<TbItem> list = tbItems.getContent();
        for (TbItem tbItem : list) {
            System.out.println(tbItem.getTitle());
        }*/

        //生成Query查询
        /*SimpleQuery query = new SimpleQuery("*:*");*/
        //封装查询条件
        /*Criteria criteria = new Criteria("item_keywords"); //搜索复制域
        criteria = criteria.contains(keywords);

        query.addCriteria(criteria);*/

        SimpleQuery query = new SimpleQuery("*:*");
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        List<TbItem> list = tbItems.getContent();
        for (TbItem tbItem : list) {
            System.out.println(tbItem.getTitle());
        }


        Map resultMap = new HashMap<>();
        resultMap.put("itemList", list);
        System.out.println("查询到"+list.size()+"条数据");
        return resultMap;
    }
}
