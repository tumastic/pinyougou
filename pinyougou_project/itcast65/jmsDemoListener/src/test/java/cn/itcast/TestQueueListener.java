package cn.itcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration("classpath:applicationContext-activemq-consumer.xml")
    public class TestQueueListener {

        @Test
        public void testListener(){
            while (true){}

        }
    }


