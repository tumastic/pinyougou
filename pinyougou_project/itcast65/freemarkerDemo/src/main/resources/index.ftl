<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<#--assign name='阴阳师'-->
<#if name??>
   测试写出文件1234:${name}<br>
</#if>
<#--测试写出文件:${name!'默认值'}<br>-->

<#if success='true'>
    恭喜您抽到SSR!<br>
<#elseif success='false'>
    恭喜您抽到SP!<br>
<#else>
    恭喜您抽到R!<br>
</#if>



<#list goodsList as goods>
${goods_index+1}商品名称：${goods.name} | 商品价格:${goods.price}<br>
</#list>
一共有${goodsList?size}条商品<br>

${price?c}<br>
<#include "head.ftl"><br>

${timenow?string("yyyy年MM月dd日 HH:mm:ss")}



</body>
</html>