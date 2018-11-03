package com.pinyougou;

import cn.itcast.test.SpringQueueProduce;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-activemq-producter.xml")
public class TestQueue {

    @Autowired
    private SpringQueueProduce springQueueProduce;

    @Test
    public void testSendMessage(){
        springQueueProduce.sendTextMessage("测试发送的spring的jms");
    }
}
