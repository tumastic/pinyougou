package com.pinyougou.search.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

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

        //SimpleHighlightQuery有对结果进行高亮显示的效果，替代SimpleQuery
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //========================设置分页参数起始记录数&每页记录数======================
        Integer  page = (Integer) searchMap.get("page");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        query.setOffset((page - 1)*pageSize); //设置分页的起始记录数
        query.setRows(pageSize);   //设置每页返回的条数

        //=================声明高亮选项==================
        HighlightOptions options = new HighlightOptions();
        options.addField("item_title");
        options.setSimplePrefix("<span style='color:red'>"); // 高亮头部
        options.setSimplePostfix("</span>"); // 高亮尾部
        query.setHighlightOptions(options);

       Criteria criteria =  new Criteria("item_keywords");
       criteria = criteria.contains(keywords);
        query.addCriteria(criteria);

        //===================根据分类进行过滤查询=================
        String categoryStr = (String)searchMap.get("category");
        if(categoryStr!=null&&categoryStr.length()>0){//前端传入了分类的过滤条件
            System.out.println("分类=="+categoryStr);
            //过滤查询条件
            Criteria criteria1 = new Criteria("item_category");//指定过滤的域
            criteria = criteria1.contains(categoryStr);
            //创建过滤查询
            SimpleFacetQuery filterQuery = new SimpleFacetQuery(criteria);
            query.addFilterQuery(filterQuery);//将过滤查询添加到SimpleHighlightQuery查询中
        }
        //=====================根据品牌进行过滤查询====================
        String brandStr = (String) searchMap.get("brand");
        if(brandStr!=null && brandStr.length() > 0){ //前端传入了分类的过滤条件
            System.out.println("品牌==="+brandStr);
            //过滤查询的条件
            Criteria cariteria = new Criteria("item_brand"); //指定过滤的域
            cariteria = cariteria.contains(brandStr);
            //创建过滤查询
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(cariteria);
            query.addFilterQuery(filterQuery);//将过滤查询添加到SimpleHighlightQuery查询中
        }
        //=====================规格的过滤条件查询============================
        Map<String,String> specMap = (Map<String, String>) searchMap.get("spec");
        if(specMap !=null){
            for (String key : specMap.keySet()) {
                Criteria criteria1 = new Criteria("item_spec_" + key);
                criteria1 = criteria1.contains(specMap.get(key)); //根据传入的规格关键字进行规格条件的封装
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteria1);
                query.addFilterQuery(filterQuery);
            }
        }
        //=========================价格的过滤条件查询==============================
        String priceStr = (String) searchMap.get("price");
        if(priceStr!=null && priceStr.length() > 0){
            String[] prices = priceStr.split("-");
            //取价格的起始值
            //if(!prices[0].equals("0")){ //起始值的判断可以忽略
            //大于等于的查询起始值
            Criteria criteria1 = new Criteria("item_price");
            criteria1 = criteria1.greaterThanEqual(prices[0]); //大于等于
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteria1);
            query.addFilterQuery(filterQuery);
            //}
            //取价格的结束值
            if(!prices[1].equals("*")){  //结束值必须判断
                //进行小于等于的查询
                Criteria criteria2 = new Criteria("item_price");
                criteria2 = criteria2.lessThanEqual(prices[1]); //大于等于
                SimpleFilterQuery filterQuery1 = new SimpleFilterQuery(criteria2);
                query.addFilterQuery(filterQuery1);
            }
        }
        //对价格进行排序操作
        String sortValue = (String) searchMap.get("sort");
        if(sortValue.equals("ASC")){
            Sort sort = new Sort(Sort.Direction.ASC, "item_price");
            query.addSort(sort); //将排序添加到HighlightQuery中
        }else{
            Sort sort = new Sort(Sort.Direction.DESC, "item_price");
            query.addSort(sort); //将排序添加到HighlightQuery中
        }




        //再进行查询---分页查询
        HighlightPage<TbItem> items = solrTemplate.queryForHighlightPage(query, TbItem.class);
        List<TbItem> list = items.getContent(); //content没有高亮结果,获取总记录


        //items->Highlights->Snipplets 再获取Snipplets集合中的第0个对象取到高亮后的效果设置给title
        for (TbItem tbItem : list) {  //所以进行循环获取高亮结果赋值给title属性
            System.out.println(tbItem.getTitle());
            //确定Highlights数组必须有高亮结果
            if(items.getHighlights(tbItem)!=null && items.getHighlights(tbItem).size() >0){
                HighlightEntry.Highlight highlight = items.getHighlights(tbItem).get(0);   //先获取highlights数组
                List<String> snipplets = highlight.getSnipplets();   //再获取snipplets数组,高亮数组
                //确定Highlights中的Snipplets数组必须有高亮效果
                if(snipplets!=null&&snipplets.size()>0){
                    tbItem.setTitle(snipplets.get(0));//将有高亮结果的title从snipplets中获取到再次设置给title中 new8- <span style='color:red'>三星</span> W999 黑色 电信3G手机 双卡双待双通
                }
            }
        }
        /*将查询的title标为高亮,但是在list中返回的并不是高亮数据,因此需要将高亮数据再次填入TbItem.
          对于单个数据简单来说就是items.getHighlights(tbItem).get(0).getSnipplets().get(0);就能得到一个高亮的数据,再setTitle即可
         */



        Map resultMap = new HashMap<>();
        resultMap.put("itemList", list);
        //总记录数封装到返回的resultMap中
        resultMap.put("total", items.getTotalElements());

        System.out.println("查询到"+list.size()+"条数据");
        return resultMap;

    }
}
