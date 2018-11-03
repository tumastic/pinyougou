package com.pinyougou.content.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 * @author Administrator
 *
 */

/*
   在进行广告的变更的时候需要进行添加和修改(add和update,delete),在改动之前需要进行缓存同步,删除掉原来的数据,再将数据提交
   Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();//广告分类ID
		redisTemplate.boundHashOps("content").delete(categoryId);
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		//redisTemplate.boundHashOps("content").delete(content.getCategoryId());
		contentMapper.insert(content);
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
		//Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();//防止修改的content缺少CategoryId数据
		//	redisTemplate.boundHashOps("content").delete(categoryId);
		contentMapper.updateByPrimaryKey(content);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			//Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();
			//		redisTemplate.boundHashOps("content").delete(categoryId);
			contentMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	//判读redis是否有值,如果有就直接使用,没有就从数据库中查询并保存到redis中
	@Override
	public List<TbContent> findByCategoryId(Long categoryId) {

	    //直接从数据库进行查询数据，现在修改为先从redis中查询数据，
        List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
       //boundHashOps是实现哈希结构的一种方法,'名',('key','value')
        if(contentList==null) {//如果没有查询数据库
            TbContentExample example = new TbContentExample();
            //判断广告状态
            //并且进行排序
            example.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");
            example.setOrderByClause("sort_order desc"); //字段名不是属性名
            contentList = contentMapper.selectByExample(example);

            //并且保存到redis中
            redisTemplate.boundHashOps("content").put(categoryId, contentList);
            System.out.println("从数据库获取");
        }else{
            System.out.println("从redis获取");
        }

		return contentList;
	}

}
