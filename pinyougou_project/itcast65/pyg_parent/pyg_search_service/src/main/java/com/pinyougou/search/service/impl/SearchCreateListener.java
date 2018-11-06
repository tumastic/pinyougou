package com.pinyougou.search.service.impl;


import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class SearchCreateListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage oMessage = (ObjectMessage) message;

        try {
            Long[] ids = (Long[]) oMessage.getObject();
            itemSearchService.addItemToSolr(ids);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
