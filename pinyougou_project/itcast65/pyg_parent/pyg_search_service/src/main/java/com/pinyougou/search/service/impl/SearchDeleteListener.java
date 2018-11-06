package com.pinyougou.search.service.impl;

import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class SearchDeleteListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage oMessage = (ObjectMessage) message;

        try {
            Long[] ids = (Long[]) oMessage.getObject();
            itemSearchService.removeItemToSolr(ids);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
