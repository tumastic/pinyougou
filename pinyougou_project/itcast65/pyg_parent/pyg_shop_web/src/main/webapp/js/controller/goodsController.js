 //控制层 
app.controller('goodsController' ,function($scope,$controller   ,goodsService,itemCatService,typeTemplateService,uploadService,$location){

	$controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}
		);
	}

	//分页
	$scope.findPage=function(page,rows){
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	}

	//查询实体
	$scope.findOne=function(id){
       if( $location.search()['id']==null){ //如果为空跳过findOne查询
            return ;
        }
        id = $location.search()['id'];
       alert(id);
		goodsService.findOne(id).success(
			function(response){

                $scope.entity = response;
                //向富文本编辑器添加商品介绍
                editor.html($scope.entity.goodsDesc.introduction);
                //显示图片列表
                $scope.entity.goodsDesc.itemImages=
                    JSON.parse($scope.entity.goodsDesc.itemImages);
                //显示扩展属性
                $scope.entity.goodsDesc.customAttributeItems=
                    JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                //读取规格
                $scope.entity.goodsDesc.specificationItems=
                    JSON.parse($scope.entity.goodsDesc.specificationItems);
                //准备SKU信息
                for( var i=0;i<$scope.entity.itemList.length;i++ ){
                    $scope.entity.itemList[i].spec = JSON.parse( $scope.entity.itemList[i].spec);
                }
			}
		);
	}

	//保存
	$scope.save=function(){
		var serviceObject;//服务层对象
        $scope.entity.goodsDesc.introduction = editor.html(); //从富文本编辑器取值

		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加
		}
		serviceObject.success(
			function(response){
				if(response.success){
					alert(response.message);
				}
			}
		);
	}


	//批量删除
	$scope.dele=function(){
		//获取选中的复选框
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}
			}
		);
	}

	$scope.searchEntity={};//定义搜索对象

	//搜索
	$scope.search=function(page,rows){
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	}



	//一级商品分类的显示
    $scope.selectItemCat1List=function(){
		itemCatService.findByParentId(0).success(
			function(response){
				$scope.itemCat1List = response;
			}
		)
	}

	//根据一级的属性发生变化，进行二级分类的查询
	$scope.$watch('entity.goods.category1Id',function(newValue,oldValue){
		//进行二级列表查询
        itemCatService.findByParentId(newValue).success(
            function(response){
                $scope.itemCat2List = response;
            }
        )
	})

    //根据二级的属性发生变化，进行三级分类的查询
    $scope.$watch('entity.goods.category2Id',function(newValue,oldValue){
        //进行二级列表查询
        itemCatService.findByParentId(newValue).success(
            function(response){
                $scope.itemCat3List = response;
            }
        )
    })

    //根据三级的属性发生变化，查询分类对象，获取分类下的模本id
    $scope.$watch('entity.goods.category3Id',function(newValue,oldValue){
        //进行二级列表查询
        itemCatService.findOne(newValue).success(
            function(response){
                $scope.entity.goods.typeTemplateId = response.typeId;  //模版id赋值给封装对象
            }
        )
    })

	//根据entity.goods.typeTemplateId获取模版对象，将模版对象存入$scope.typeTemplate
	$scope.$watch('entity.goods.typeTemplateId',function(newValue,oldValue){
        typeTemplateService.findOne(newValue).success(
        	function(response){
        		$scope.typeTemplate = response;
        		$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);  //品牌字符串转品牌json对象
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
			}
		)

        typeTemplateService.findSpecList(newValue).success(
            function(response) {
                $scope.specList=response;
            }
        )
	})


    $scope.init = function(){
        $scope.selectItemCat1List();
        $scope.image_entity = {}; //定义保存图片的对象
        $scope.entity = {goodsDesc:{itemImages:[],customAttributeItems:[],specificationItems:[]}};
        $scope.findOne();
    }


    //图片上传的方法
    $scope.uploadFile = function() {
        uploadService.uploadFile().success(
            function(response) {
                if (response.success) {// 如果上传成功， 取出 url
                    $scope.image_entity.url = response.message;// 设置文件地址
                } else {
                    alert(response.message);
                }
            }).error(function() { //比如请求接口都没有的错误，上面的response.success后端还能返回
            alert("上传发生错误");
        });
    };

    // 添加图片列表
    $scope.addImage = function() {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }
    // 从图片列表中删除图片
    $scope.deleImage = function(index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    }


    //集合   需要的集合中的key   需要判断该key对应的value  如果一致返回true否则返回false
    searchObjectByKey=function(list,key,value){   //value是传入的规格的名称
        for(var i=0;i<list.length;i++){
            if(list[i][key]==value){
                return list[i];  //直接将{"attributeName":"网络制式","attributeValue":["移动3G"]}对象返回
            }
        }
        return null;
    }

	//该方法用于更新本次勾选的规格  $event：传入复选框的对象  name规格的名称   value规格的值
    $scope.updateSpecAttribute = function($event, name, value) {
        //先查询当前的集合中是否已经有当前的name了

		// $scope.entity.goodsDesc.specificationItems数组进行新增保存选择的规格和规格选项
        var object = searchObjectByKey($scope.entity.goodsDesc.specificationItems, 'attributeName',name);

        if (object != null) {
            if ($event.target.checked) {//如果已经有了，并且当前check选择了 是勾选了多选框
                object.attributeValue.push(value); //进行追加
            } else {
                // 取消勾选
                object.attributeValue.splice(object.attributeValue.indexOf(value ) ,1);//移除选项
                // 如果选项都取消了，将此条记录移除
                if (object.attributeValue.length == 0) {
                    $scope.entity.goodsDesc.specificationItems.splice(
                        $scope.entity.specifgoodsDesc.icationItems
                            .indexOf(object), 1);
                }
            }
        } else { //根据规格名称没找到该规格
            $scope.entity.goodsDesc.specificationItems.push({"attributeName" : name, "attributeValue" : [ value ]});
        }
    }


    //创建itemList集合中的数据

	//[{"attributeName":"网络","attributeValue":["移动3G","移动4G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
    $scope.createItemList = function() {
        $scope.entity.itemList = [ {spec : {},price : 0,num : 99999,status : '0',isDefault : '0'} ];// 初始
        //[{"attributeName":"网络","attributeValue":["移动3G","移动4G"]},{"attributeName":"机身内存","attributeValue":["16G"]}]
		var items = $scope.entity.goodsDesc.specificationItems;

        for (var i = 0; i < items.length; i++) { //几个规格走几遍
            $scope.entity.itemList = addColumn($scope.entity.itemList,
                items[i].attributeName, items[i].attributeValue); //第一次循环是初始化数据，第二个参数是当前规格名称，第三个参数是当前规格选项数组
        }
    }

    //$scope.entity.itemList = [{spec:{"网络制式":"移动3G",“机身内存”:16G},price:'0.01',num:'99999',status:'0',isDefault:'0'},{spec:{"网络制式":"移动4G",“机身内存”:16G},price:'0.01',num:'99999',status:'0',isDefault:'0'}]
    addColumn = function(list, columnName, conlumnValues) {
        var newList = [];// 新的集合
        for (var i = 0; i < list.length; i++) {//{spec : {},price : 0,num : 99999,status : '0',isDefault : '0'} ,有几个itemList走几遍
            var oldRow = list[i];  //获取出当前行的内容 {spec:{},price:'0.01',num:'99999',status:'0',isDefault:'0'}
            for (var j = 0; j < conlumnValues.length; j++)  {//循环attributeValue数组的内容["移动3G","移动4G"] ,有几个规格选项走几遍
                var newRow = JSON.parse(JSON.stringify(oldRow));// 深克隆,根据attributeValue的数量:先将对象转字符串：再将字符串转对象
                //{spec : {},price : 0,num : 99999,status : '0',isDefault : '0'}
                newRow.spec[columnName] = conlumnValues[j];//{spec:{"网络制式":"移动3G"},price:'0.01',num:'99999',status:'0',isDefault:'0'}
                //{spec:{"网络制式":"移动4G"},price:'0.01',num:'99999',status:'0',isDefault:'0'}
				newList.push(newRow);
            }
        }
        return newList;
    }
});
