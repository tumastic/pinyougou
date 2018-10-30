import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.*;
import entity.Goods;


public class testMethod extends BaseConfiguration{
    @Reference
    public BrandService brand;

    @Reference
    public GoodsService goods;
    @Reference
    private ItemCatService itemCat;
    @Reference
    private SellerService seller;
    @Reference
    private SpecificationService specification;


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
    }
}
