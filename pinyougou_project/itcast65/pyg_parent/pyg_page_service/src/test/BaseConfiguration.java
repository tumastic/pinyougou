import com.pinyougou.page.service.ItemPageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@ContextConfiguration(locations = {"classpath*:spring/applicationContext-*.xml"})
public class BaseConfiguration {

    @Autowired(required = false)
   private ItemPageService itemPageService;

    @Test
    public void test(){
        try{
            itemPageService.createItemHtml(149187842867935l);
            System.out.println("success");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error");
        }
    }

}
