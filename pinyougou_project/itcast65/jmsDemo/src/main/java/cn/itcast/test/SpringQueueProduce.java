package cn.itcast.test;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class SpringQueueProduce {

    @Autowired
    private ActiveMQQueue destination;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendTextMessage(final String text){
        //务必设置发送消息的目标
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage textM = session.createTextMessage(text);
                return textM;
            }
        });

    }
}
