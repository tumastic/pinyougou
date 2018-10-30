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
@ContextConfiguration(locations = {"/applicationContext-test.xml"})
public class BaseConfiguration {

   @Autowired
    public BrandService brand;

    @Autowired
    public GoodsService goods;
    @Autowired
    private ItemCatService itemCat;
    @Autowired
    private SellerService seller;
    @Autowired
    private SpecificationService specification;
    @Autowired
    private TypeTemplateService typeTemplate;
    @Autowired
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
