package com.pinyougou.cart.service;

import entity.Cart;

import java.util.List;

public interface CartService {
     //cart是一张集合表包含  选中的sku属性  商家id属性  商家名称
    // 添加商品到购物车列表(1.要加商品的列表  2.加入商品的id  3.加入商品数量)
    public List<Cart> addItemToCartList(List<Cart> cartList, Long itemId, Integer num);
     //用户名作为key
    //根据key获取redis中的购物车
    public List<Cart> findCartListFromRedis(String name);

    //根据key存redis购物车
    public void saveCartListToRedis(String username, List<Cart> cartList);

    //合并两个购物车的集合，返回合并后的购物车
    List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);

    //删除购物车的方法
    public void delCartListToRedis(String key);
}
