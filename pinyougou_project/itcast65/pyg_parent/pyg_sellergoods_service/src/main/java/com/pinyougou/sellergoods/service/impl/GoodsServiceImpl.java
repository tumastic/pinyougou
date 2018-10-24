package com.pinyougou.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private TbSellerMapper sellerMapper;

	@Autowired
	private TbBrandMapper brandMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		//商品新增
		TbGoods gs = goods.getGoods();
		//商品状态默认值的处理
		gs.setAuditStatus("0");  //0：未审核商品  1：已审核
		goodsMapper.insert(gs);
		//商品详情的新增
		TbGoodsDesc goodsDesc = goods.getGoodsDesc();
		goodsDesc.setGoodsId(gs.getId()); //维护一对一关系
		goodsDescMapper.insert(goodsDesc);


		//判断是否勾选了规格勾选框
		if("1".equals(gs.getIsEnableSpec())){
			//保存所有的itemlist
			List<TbItem> itemList = goods.getItemList();

			for (TbItem item : itemList) {
				//sku的标题的时候需要拼接商品名称+当前sku的所有规格
				String itemTitle = gs.getGoodsName(); //商品名称
				Map<String,String> specMap = JSON.parseObject(item.getSpec(), Map.class);//{"机身内存":"16G","网络":"联通3G"}
				for (String key : specMap.keySet()) {
					itemTitle += " " +specMap.get(key);
				}

				item.setTitle(itemTitle); //商品标题

				//卖点
				item.setSellPoint(gs.getCaption());

				//图片（取第一张goodsDesc）
				List<Map> images = JSON.parseArray(goodsDesc.getItemImages(), Map.class);
				if(images.size() >0){
					item.setImage(images.get(0).get("url").toString());
				}

				//三级分类id，三级分类名字
				item.setCategoryid(gs.getCategory3Id());

				TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(gs.getCategory3Id());
				item.setCategory(itemCat.getName());

				//创建时间，修改时间
				item.setCreateTime(new Date());
				item.setUpdateTime(new Date());

				//对应的商品id
				item.setGoodsId(gs.getId());

				//商家id,商家名称
				item.setSellerId(gs.getSellerId());

				TbSeller seller = sellerMapper.selectByPrimaryKey(gs.getSellerId());
				item.setSeller(seller.getName());

				//品牌名称
				TbBrand brand = brandMapper.selectByPrimaryKey(gs.getBrandId());

				item.setBrand(brand.getName());

				itemMapper.insert(item);
			}
		}else{ //没有规格的商品需要手动新增一个sku对象
			TbItem item = new TbItem();
			item.setTitle(gs.getGoodsName()); //商品标题

			//卖点
			item.setSellPoint(gs.getCaption());

			//图片（取第一张goodsDesc）
			List<Map> images = JSON.parseArray(goodsDesc.getItemImages(), Map.class);
			if(images.size() >0){
				item.setImage(images.get(0).get("url").toString());
			}

			//三级分类id，三级分类名字
			item.setCategoryid(gs.getCategory3Id());

			TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(gs.getCategory3Id());
			item.setCategory(itemCat.getName());

			//创建时间，修改时间
			item.setCreateTime(new Date());
			item.setUpdateTime(new Date());

			//对应的商品id
			item.setGoodsId(gs.getId());

			//商家id,商家名称
			item.setSellerId(gs.getSellerId());

			TbSeller seller = sellerMapper.selectByPrimaryKey(gs.getSellerId());
			item.setSeller(seller.getName());

			//品牌名称
			TbBrand brand = brandMapper.selectByPrimaryKey(gs.getBrandId());

			// {spec : {},price : 0,num : 99999,status : '0',isDefault : '0'}
			item.setPrice(gs.getPrice());
			item.setNum(9999);
			item.setStatus("1");
			item.setIsDefault("1");

			item.setBrand(brand.getName());


			itemMapper.insert(item);
		}

	}


	
	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		goodsMapper.updateByPrimaryKey(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){

		Goods goods=new Goods();
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setGoods(tbGoods);
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(tbGoodsDesc);

		//在public Goods findOne(Long id) 方法中增加，加载sku信息的内容
		TbItemExample example = new TbItemExample();
		com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);// 查询条件： 商品 ID
		List<TbItem> itemList = itemMapper.selectByExample(example);
		goods.setItemList(itemList);

		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			//物理删除
			//goodsMapper.deleteByPrimaryKey(id);

			//修改为逻辑删除
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setIsDelete("1");
			goodsMapper.updateByPrimaryKey(goods);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();

		//逻辑删除查询条件
			criteria.andIsDeleteIsNull(); //没被逻辑删除的所有商品，如果赋值isDelete为1，新增商品后的isDelete为null
		
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


	public void updateStatus(Long[] ids, String status) {
		for (Long id : ids) {
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(goods);
		}
	}
}
