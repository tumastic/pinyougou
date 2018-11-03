import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.*;
import entity.Goods;
import entity.Specification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
//@ContextConfiguration(locations = {"classpath*:spring/applicationContext-*.xml"})
public class BaseConfiguration {

   @Autowired(required = false)
    public BrandService brand;

    @Autowired(required = false)
    public GoodsService goods;
    @Autowired(required = false)
    private ItemCatService itemCat;
    @Autowired(required = false)
    private SellerService seller;
    @Autowired(required = false)
    private SpecificationService specification;
    @Autowired(required = false)
    private TypeTemplateService typeTemplate;
    @Autowired(required = false)
    private UserService user;


    public void testFindOne(String clazz,Long num){
        if(clazz.equals("brand")){
            TbBrand one = brand.findOne(num);
            System.out.println(one);
        }if(clazz.equals("goods")){
            Goods one = goods.findOne(num);
            System.out.println(one);
        }if(clazz.equals("itemCat")) {
            TbItemCat one = itemCat.findOne(num);
            System.out.println(one);
        }
        if(clazz.equals("seller")) {
            TbSeller one = seller.findOne(num + "");
            System.out.println(one);
        }
        if(clazz.equals("specification")) {
            Specification one = specification.findOne(num);
            System.out.println(one);
        }
        if(clazz.equals("typeTemplate")) {
            TbTypeTemplate one = typeTemplate.findOne(num);
            System.out.println(one);
        }
        if(clazz.equals("user")) {
            TbUser one = user.findOne(num);
            System.out.println(one);
        }
    }

}
