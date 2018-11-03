package cn.itcast.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicProduce {
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("itcast62_topic");
        MessageProducer producer = session.createProducer(topic);
        //7.创建消息   				 //接受消息setMessageListener
        for (int i = 0; i < 10; i++) {
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("username", "cgx");
            mapMessage.setString("password", "123456");
            //8.使用生成者发送消息  		 //等待键盘输入System.in.read();
            producer.send(mapMessage);
        }
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
        System.out.println("==发送完毕==");
    }
}
