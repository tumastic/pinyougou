package cn.itcast.test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueProducer {

    public static void main(String[] args) throws JMSException {
        //1.创建连接工厂
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        //2.获取连接
        Connection connection = factory.createConnection();
        //3.启动连接
        connection.start();
        //4.获取session  参数1：事务   参数2：应答模式
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        //5.创建队列对象
        Queue itcast62 = session.createQueue("itcast62");
        //6.创建消息生产者对象或消费者
        MessageProducer producer = session.createProducer(itcast62);
        //7.创建消息   				 //接受消息setMessageListener
        for (int i = 0; i < 10; i++) {
            TextMessage message = session.createTextMessage("测试发送消息"+i);
            //8.使用生成者发送消息  		 //等待键盘输入System.in.read();
            producer.send(message);
        }

        session.commit();
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
        System.out.println("==发送完毕==");
    }
}
