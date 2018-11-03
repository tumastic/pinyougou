package cn.itcast.demo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class MessageListener implements javax.jms.MessageListener {

    public void onMessage(Message message) {
        TextMessage ms = (TextMessage) message;
        try {
            System.out.println("监听到="+ms.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
