package com.pinyougou.order.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.pojo.TbOrderItem;
import entity.Cart;
import org.apache.activemq.transport.tcp.TcpBufferedOutputStream;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderExample;
import com.pinyougou.pojo.TbOrderExample.Criteria;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;
import util.IdWorker;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	/*
	   获取购物车信息,遍历大目录--商家,定位到每一个商家下,将页面传入的order数据注入到order表中,比如创建时间,创建人,订单等订单流程状态信息.
	    为每个商家设定一个订单号
	    遍历每个商家所属的商品,将orderid注入,并将原始id进行更换,变为条形码id,注入到orderItem中,并将该商户的结算额计算出来,传到order表中
	 */
	@Override
	public void add(TbOrder order) {
		//生成订单
		//根据购物车生成订单，一个cart一个order

		//从redis中取出购物车
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());

		for (Cart cart : cartList) {
			TbOrder tbOrder = new TbOrder();
			//id，
			long orderId = idWorker.nextId(); //订单ID

			tbOrder.setOrderId(orderId);
			tbOrder.setStatus("1");// '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价'
			tbOrder.setCreateTime(new Date());
			tbOrder.setUpdateTime(new Date());
			tbOrder.setSourceType("2"); //'订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端',
			tbOrder.setSellerId(cart.getSellerId());

			tbOrder.setPaymentType(order.getPaymentType());//支付类型(页面只绑定了支付方式)
			tbOrder.setReceiver(order.getReceiver());//收货人
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());//收货人地址
			tbOrder.setReceiverMobile(order.getReceiverMobile());//收货人手机号

			tbOrder.setUserId(order.getUserId()); //谁下的订单


			//循环购物车中的明细记录
			double money=0;
			//订单生成的同时，保存订单详情
			List<TbOrderItem> orderItemList = cart.getOrderItemList();//获取购物车明细,一个商户为一个cart
			for (TbOrderItem orderItem : orderItemList) {
				orderItem.setOrderId(orderId); //维护多对一关系
				//采用雪花算法作为商品明细的主键
				orderItem.setId(idWorker.nextId()); //再次利用雪花id生成器，生成新的id   可能是为了作为条形码使用
				orderItemMapper.insert(orderItem);

				money += orderItem.getTotalFee().doubleValue();//金额累加
			}

			tbOrder.setPayment(new BigDecimal(money));//订单合计数
			orderMapper.insert(tbOrder);

		}

		//删除掉当前用户下的redis中购物车
		redisTemplate.boundHashOps("cartList").delete(order.getUserId());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
