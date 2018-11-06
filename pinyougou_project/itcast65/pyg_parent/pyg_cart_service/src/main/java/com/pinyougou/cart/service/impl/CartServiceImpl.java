package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.cart.service.CartService;
import entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbItemMapper itemMapper;


    /*
       购物车增加思路
       1.根据sku找大分类--商家,判读当前购物车里是否包含所需要的商家
         如果没有 ,利用sku反向获得商家的id和名,sku 加到cart中
         如果有,判断sku在购物车中是否存在
               不存在,直接在orderItemList中直接添加
               存在,找到购物车中的sku对他的num和price进行更新
                    更新后判断操作的num数值是否大于0,
                       如果不大于,就删除该条目
                    判断orderItemList的长度,也就是判断当前商户所属的商品是否存在
                       如果不存在就删除该cart
     */




    @Override      //购物车中的sku进行增加和减少
    public List<Cart> addItemToCartList(List<Cart> cartList, Long itemId, Integer num) {
        //首先根据itemId获取购物车列表中的商家信息,进行对比
        //itemId是sku的数据，获取sellerId
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        String sellerId = item.getSellerId();

        //根据sellerId查找购物车中的是否已经存在该商家
        Cart cart = searchCartListBySellerId(cartList, sellerId);


        if(cart==null){//没有商家的当前购物车

            cart = new Cart();

            TbOrderItem orderItem = createOrderItem(item,num); //商品明细

            List<TbOrderItem> orderItems = new ArrayList<>();
            orderItems.add(orderItem);

            cart.setOrderItemList(orderItems);

            cart.setSellerId(sellerId);  //商家id
            cart.setSellerName(item.getSeller()); //商家名称
            cartList.add(cart);
        }else{
            //如果找到该商家，再根据itemId查找该购物车是否有该商品

            TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
            if(orderItem==null) { //没找到该商品对象,将sku放入购物车

                orderItem = createOrderItem(item,num); //重新创建商品明细，赋值给购物车中的明细数组
                cart.getOrderItemList().add(orderItem);
            }else{ //找到
                //直接增加商品中的数量
                orderItem.setNum(orderItem.getNum()+num); //将数量不管正负都相加
                //重新计算总金额
                orderItem.setTotalFee(new BigDecimal(orderItem.getNum() * orderItem.getPrice().doubleValue()));

                //判断数量是否小于1
                if(orderItem.getNum() < 1){
                    //从cart中的商品明细数组中移除掉当前的商品对象
                    cart.getOrderItemList().remove(orderItem);
                }
                //再判断cart中的商品明细集合是否小于1，如果小于1，将cart从cartList中移除
                if(cart.getOrderItemList().size() < 1){
                    cartList.remove(cart);
                }
            }
        }
        return cartList;
    }



    //根据sellerId查找购物车中的是否已经存在该商家
    private Cart searchCartListBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if(cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }

    //根据该商品id查找购物车中的商品明细,返回查找到的商品对象
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId){
        for (TbOrderItem tbOrderItem : orderItemList) {
            if(itemId.equals(tbOrderItem.getItemId())){ //比较sku的id是否已经存在了
                return tbOrderItem;
            }
        }
        return null;
    }

    //根据tbitem创建tborderitem
    private TbOrderItem createOrderItem(TbItem item, Integer num){

        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId()); //根据sku的商品id设置订单明细中的商品id
        orderItem.setItemId(item.getId());         //根据sku的id设置订单明细中的skuId
        orderItem.setNum(num);                      //设置够买的数量
        orderItem.setPicPath(item.getImage());      //设置图片路径
        orderItem.setPrice(item.getPrice());        //设置价格，是sku中的价格
        orderItem.setSellerId(item.getSellerId());   //设置商品明细中的商家id，来源于sku中的商家id
        orderItem.setTitle(item.getTitle());          //根据sku中的title设置商品明细中的title
        //BigDecimal用来适应各种精度的运算
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num)); //总金额是金额*数量
        return orderItem;
    }

    @Override   //name都是uuid做的临时值
    public List<Cart> findCartListFromRedis(String name) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(name);

        //防止空指针异常  !!
        if(cartList == null){ //防止添加购物车数据为空
            cartList = new ArrayList<Cart>();
        }
        return cartList;
    }

    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        redisTemplate.boundHashOps("cartList").put(username, cartList);
    }

    @Override
    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
        for (Cart cart : cartList1) {
            List<TbOrderItem> orderItemList = cart.getOrderItemList();

            for (TbOrderItem orderItem : orderItemList) {
                cartList2 = addItemToCartList(cartList2,orderItem.getItemId(),orderItem.getNum());
            }
        }
        return cartList2;
    }

    @Override
    public void delCartListToRedis(String key) {
        redisTemplate.boundHashOps("cartList").delete(key);
    }

}
