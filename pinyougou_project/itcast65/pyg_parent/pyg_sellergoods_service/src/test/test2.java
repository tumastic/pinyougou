import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.*;
import com.pinyougou.sellergoods.service.impl.BrandServiceImpl;
import com.pinyougou.sellergoods.service.impl.ItemCatServiceImpl;
import com.pinyougou.sellergoods.service.impl.SellerServiceImpl;
import com.pinyougou.sellergoods.service.impl.SpecificationServiceImpl;
import entity.Goods;
import entity.PageResult;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class test2 extends BaseConfiguration {


    @Test
    public  void  testFindOne() {
       super.testFindOne("goods",1l);
    }








  /*  @Test
    public void testFindOne()  {

        *//*TbBrand tbBrand = brandService.findOne(2L);
        System.out.println(tbBrand);*//*
       TbGoods tbGoods = new TbGoods();
       Goods good1s =new Goods();
       Long[] ids = {1l,2l,3l};

        Goods one = goods.findOne(1L);
        List<TbGoods> all = goods.findAll();
        PageResult page = goods.findPage(1, 2);
        PageResult page1 = goods.findPage(tbGoods, 1, 1);
        goods.delete(ids);
        goods.add(good1s);
        goods.update(tbGoods);
        goods.updateStatus(ids,"1");
        System.out.println("findOne:"+one);
        System.out.println("findAll:"+all);
        System.out.println("findPage:"+page);
        System.out.println("findPage:"+page1);
        for(long a:ids){
            Goods goods1 = goods.findOne(a);
            System.out.println("delete:"+goods1);
        }

        System.out.println("add:"+all);


    }*/

}
