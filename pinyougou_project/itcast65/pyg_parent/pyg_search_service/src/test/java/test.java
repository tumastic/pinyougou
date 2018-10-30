import com.pinyougou.search.service.ItemSearchService;
import com.pinyougou.search.service.impl.ItemSearchServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-solr.xml")
public class test {
    @Autowired
    private ItemSearchServiceImpl itemSearchService;
    @Test
    public void test1(){
        Map<String ,String>map = new HashMap<>();
        Map search = itemSearchService.search(map);
        System.out.println(search.get(1));
    }


}
