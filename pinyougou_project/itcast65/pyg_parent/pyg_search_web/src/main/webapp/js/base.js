var app=angular.module('pinyougou',[]);
//angularjs修改base.js，为了能输出html，用到$sce服务的trustAsHtml方法来实现转换
app.filter('trustHtml',['$sce',function($sce){
    return function(data){
        return $sce.trustAsHtml(data);
    }
}]);