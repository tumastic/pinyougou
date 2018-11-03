app.controller("itemController",function($scope,$http){
	$scope.num = 1;

	$scope.addNum = function(x){
		$scope.num += x;
		if($scope.num < 1){  //保证够买数量必须大于0
			$scope.num = 1;
		}
	}


	$scope.specificationItems={};//记录用户选择的规格

	//用户选择规格存入到specificationItems的Map中
	$scope.selectSpecification=function(key,value){	
		$scope.specificationItems[key]=value;

		searchSku();//每次匹配规格查询一下
	}	

	//判断某规格选项是否被用户选中，判断key中保存的value是否一致，一致返回true
	$scope.isSelected=function(key,value){
		if($scope.specificationItems[key]==value){
			return true;
		}else{
			return false;
		}		
	}


	$scope.sku = {};  //默认的tbItem对象


	$scope.loadSku=function(){
			$scope.sku= skuList[0]; //为了让默认选中第一个
			$scope.specificationItems=JSON.parse(JSON.stringify($scope.sku.spec));
			//为了让SKU部分也是默认得值，进行的赋值
	}


	//循环页面上的所有skuList找到当前规格相匹配的对象赋值给$scope.sku对象
	searchSku=function(){
		for(var i=0;i< skuList.length;i++  ){
			if( matchObject (skuList[i].spec, $scope.specificationItems ) ){
				$scope.sku=skuList[i];
				return;
			}		
		}
	}


	//前端比较两个map是否key都相同
	matchObject=function(map1,map2){		
		for(var k in map1){
			if(map1[k]!=map2[k]	){
				return false;
			}				
		}
		for(var k in map2){
			if(map1[k]!=map2[k]	){
				return false;
			}				
		}
		return true;		
	}
});