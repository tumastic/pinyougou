package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.cart.service.CartService;
import entity.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;


    //从cookie中获取唯一的key
    private String getSingleUUid(){
        //从cookie中获取uuid
        String uuidCookie = CookieUtil.getCookieValue(request, "uuidCookie", "utf-8");
        if(uuidCookie==null||uuidCookie.equals("")){ //cookie中没有uuid
            uuidCookie = UUID.randomUUID().toString();
            //存到cookie中
            CookieUtil.setCookie(request, response, "uuidCookie", uuidCookie,24*60*60,"utf-8");
        }
        return uuidCookie;
    }


    @Reference
    private CartService cartService;


    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        String singleUUid = getSingleUUid();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("==登录用户名=="+name);
        List<Cart>cartList;
        if("anonymousUser".equals(name)){
            cartList=cartService.findCartListFromRedis(singleUUid);
        }else{
            cartList = cartService.findCartListFromRedis(name);
            List<Cart>uuidCartList=cartService.findCartListFromRedis(singleUUid);
            if(uuidCartList.size()>0){
                cartList  = cartService.mergeCartList(uuidCartList,cartList);
                cartService.delCartListToRedis(singleUUid);
                //存入合并后的购物车到redis
                cartService.saveCartListToRedis(name,cartList);
            }
        }
        return cartList;
    }


    @RequestMapping("/addItemToCartList")
    @CrossOrigin(origins="http://localhost:9105",allowCredentials = "true")
    public Result addItemToCartList(Long itemId, Integer num){

         /* //解决方法一：http://localhost:9109也可设置为*，但是*号不能用cookie,允许访问的域
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
        //----如果用了cookie信息-----，必须加后面这句话，如果不用cookie可以不加这句话
        response.setHeader("Access-Control-Allow-Credentials", "true");*/


        String singleUUid;
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if("anonymousUser".equals(name)) {//用户未登录
            singleUUid = getSingleUUid();  //未登录的id
        }else{
            singleUUid = name;   //登录后的用户名
        }

        try {
            List<Cart> cartList = findCartList();

            //添加到内存中的购物车列表中
            List<Cart> carts = cartService.addItemToCartList(cartList, itemId, num);

            //把添加好商品的新购物车存入到redis中
            cartService.saveCartListToRedis(singleUUid, carts);

            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }


}
