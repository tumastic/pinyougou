package com.pinyougou.search.service;

import java.util.Map;

public interface ItemSearchService {

    public Map search(Map searchMap);
    public void addItemToSolr(Long[] goodsId);
    public void removeItemToSolr(Long[] goodsId);

}
