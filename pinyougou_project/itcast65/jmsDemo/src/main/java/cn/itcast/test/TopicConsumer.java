package cn.itcast.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Map;

public class TopicConsumer {

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic itcast62_topic = session.createTopic("itcast62_topic");
        MessageConsumer consumer = session.createConsumer(itcast62_topic);
        /* TextMessage receive = (TextMessage) consumer.receive(); //手动接收消息*/

        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                try {
                    MapMessage mapMessage = (MapMessage) message;
                    System.out.println("接收到的消息3=="+mapMessage.getString("username"));
                    System.out.println("接收到的消息3=="+mapMessage.getString("password"));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        while(true){}
        //已经走不到
       /* consumer.close();
        session.close();
        connection.close();*/
    }
}
