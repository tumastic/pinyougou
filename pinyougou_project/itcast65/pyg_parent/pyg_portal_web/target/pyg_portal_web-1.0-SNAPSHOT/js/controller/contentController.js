//广告控制层
app.controller("contentController",function($scope,contentService){

    $scope.init = function(){
        $scope.findByCategoryId(1);
    }

    $scope.contentList=[];//广告集合先初始化下
    $scope.findByCategoryId=function(categoryId){
        contentService.findByCategoryId(categoryId).success(
            function(response){
                $scope.contentList[categoryId]=response; //通过分类ID区别广告分类
            }
        );
    }
});