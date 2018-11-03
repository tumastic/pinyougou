import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.sellergoods.service.impl.BrandServiceImpl;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-test.xml"})
public class test {
    @Autowired
    private TbBrandMapper tbBrandMapper;
    @Reference
    private BrandService brandService;




    @Test
    public void testFindOne(){
       /* TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(1L);
        TbBrand tbBrand1 = tbBrandMapper.selectByPrimaryKey(2L);
        BrandServiceImpl brandService  = Mockito.mock(BrandServiceImpl.class);
        //为方法返回值设计一个假值,再调用这个方法判断该方法是否正常.
        Mockito.when(brandService.findOne(1L)).thenReturn(tbBrand1);
        System.out.println(brandService.findOne(1L));
        //Assert.assertEquals(brandService.findOne(2L),tbBrand1);*/

       // TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(1L);
        TbBrand tbBrand = brandService.findOne(5L);
        System.out.println(tbBrand);

    }
}
