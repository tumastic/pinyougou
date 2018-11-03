package cn.itcast.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueConsumer {
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = factory.createConnection();
        connection.start();
        //获取session(参数一 :是否开启事务,参数二 :消息确认模式)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//自动确认
        Queue itcast62 = session.createQueue("itcast62");
        MessageConsumer consumer = session.createConsumer(itcast62);
        /*TextMessage receive = (TextMessage) consumer.receive(); //手动接收消息*/

       consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("接收到的消息123=="+textMessage.getText());
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
