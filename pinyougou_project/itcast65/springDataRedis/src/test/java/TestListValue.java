import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class TestListValue {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue(){
        redisTemplate.boundListOps("testList").leftPush("酒吞童子");
        redisTemplate.boundListOps("testList").leftPush("姑获鸟");
        redisTemplate.boundListOps("testList").leftPush("打火机");
        redisTemplate.boundListOps("testList").leftPush("打火机");
    }

    @Test
    public void getValue(){

       /* String str = (String) redisTemplate.boundListOps("testList").rightPop();
        System.out.println(str);*/

        List<String> list = redisTemplate.boundListOps("testList").range(0, -1);
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void delValue(){
        //删除大key全删
        //redisTemplate.delete("testHash");
        //redisTemplate.boundListOps("testList").
        redisTemplate.boundListOps("testList").remove(2, "打火机");
    }
}
