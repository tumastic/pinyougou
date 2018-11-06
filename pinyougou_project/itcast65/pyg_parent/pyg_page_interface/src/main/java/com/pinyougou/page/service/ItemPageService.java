package com.pinyougou.page.service;

public interface ItemPageService {
    //生成静态页面方法
    public boolean createItemHtml(Long goodsId);
    public void deleItemHtml(Long[] goodsId);
}
