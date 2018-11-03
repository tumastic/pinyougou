import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class TestValue {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue(){
        redisTemplate.boundValueOps("itcast62").set("hello");
    }

    @Test
    public void getValue(){
        String str = (String) redisTemplate.boundValueOps("itcast62").get();
        System.out.println(str);
    }

    @Test
    public void delValue(){
        redisTemplate.delete("itcast62");
    }
}
