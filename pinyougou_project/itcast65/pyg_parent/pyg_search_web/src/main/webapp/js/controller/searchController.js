app.controller('searchController',function(searchService,$scope){

    $scope.init = function(){
        $scope.searchMap={keywords:'三星'}
        $scope.search();
    }

    $scope.search = function(){
        searchService.search($scope.searchMap).success(
            function(response){ //是个map对象
                $scope.list = response.itemList;
            }
        )
    }
})