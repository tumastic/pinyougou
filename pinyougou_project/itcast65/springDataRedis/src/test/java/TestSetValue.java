import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class TestSetValue {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue(){
        redisTemplate.boundSetOps("testSet").add("SSR");
        redisTemplate.boundSetOps("testSet").add("SR");
        redisTemplate.boundSetOps("testSet").add("R");
        redisTemplate.boundSetOps("testSet").add("N");
    }

    @Test
    public void getValue(){
        Set<String> sets = redisTemplate.boundSetOps("testSet").members();
        for (String key : sets) {
            System.out.println(key);
        }
    }

    @Test
    public void delValue(){
        //删除大key全删
        //redisTemplate.delete("testHash");
        redisTemplate.boundSetOps("testSet").remove("N");
    }
}
