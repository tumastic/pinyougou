import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class TestHashValue {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue(){
        redisTemplate.boundHashOps("testHash").put("typeA", "SSR");
        redisTemplate.boundHashOps("testHash").put("typeB", "SR");
        redisTemplate.boundHashOps("testHash").put("typeC", "R");
        redisTemplate.boundHashOps("testHash").put("typeD", "N");
    }

    @Test
    public void getValue(){
        Set<String> keys = redisTemplate.boundHashOps("testHash").keys();
        for (String key : keys) {
            System.out.println(key);
        }
    }

    @Test
    public void delValue(){
        //删除大key全删
        //redisTemplate.delete("testHash");
        redisTemplate.boundHashOps("testHash").delete("typeC");
    }
}
