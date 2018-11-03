app.controller('searchController',function(searchService,$scope){

    $scope.init = function(){
        //封装所有的搜索条件
        $scope.searchMap = {
            "keywords" : "三星",      // 1,主查询条件 （关键词搜索条件）
            "category" : "",          // 2,分类查询参数
            "brand" : "",	          // 3，品牌参数
            "spec" : {},	// 4,规格属性参数
            "price" : "",	// 5,价格参数
            "sort" : "ASC",	// 6,排序
            "page" : 1,		// 7,页码
            "pageSize" : 10	//8.每页显示记录数
        }

        $scope.search();
    }


    //关键词的搜索方法
    $scope.keywordsearch = function(){
        //对其他的过滤条件进行清空操作
        $scope.searchMap.category = "";
        $scope.searchMap.brand = "";
        $scope.searchMap.spec = {};
        $scope.searchMap.price = "";

        $scope.search();
    }

    //搜索方法
    $scope.search = function(){
        $scope.searchMap.pageSize= $scope.paginationConf.itemsPerPage; //从分页控件中获取每页记录数
        $scope.searchMap.page= $scope.paginationConf.currentPage;   //从分页控件中获取当前页码

        searchService.search($scope.searchMap).success(
            function(response){ //是个map对象
                $scope.list = response.itemList;
                $scope.paginationConf.totalItems = response.total; //总记录数
            }
        )
    }

    //增加过滤查询条件
    $scope.addFilterCondition = function(key,value){
        //$scope.searchMap.keywords = ""; //将关键字清空

        if(key == 'category' || key == 'brand' || key == 'price'){
            $scope.searchMap[key] = value; //进行赋值
        }else{ //如果发现是spec规格的过滤条件查询
            $scope.searchMap.spec[key] = value;
        }

        $scope.search(); //调用查询方法
    }

    //移除过滤搜索条件
    $scope.removeSearchItem=function(key){
        if(key=="category" ||  key=="brand" || key=="price"){//如果是分类或品牌价格
            $scope.searchMap[key]="";
        }else{//否则是规格
            delete $scope.searchMap.spec[key];//移除此属性 前端删除map中key的方法delete
        }
        //调用搜索方法
        $scope.search();
    }

    //价格升降序方法
    $scope.sortSearch = function(sort){
        $scope.searchMap.sort=sort;
        $scope.search();
    }

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
           // $scope.reloadList();//重新加载
            $scope.search();
        }
    };
})