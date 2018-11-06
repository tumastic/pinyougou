app.controller('cartController',function($scope,cartService,addressService,orderService){
    //查询购物车列表
    $scope.findCartList=function(){
        cartService.findCartList().success(
            function(response){
                $scope.cartList=response;
                sum();
            }
        );
    }

    //购物车新增数量方法
    $scope.addItemToCartList=function(itemId,num){
        cartService.addItemToCartList(itemId,num).success(
            function(response){
                if(response.success){
                    $scope.findCartList();//刷新列表
                }else{
                    alert(response.message);//弹出错误提示
                }
            }
        );
    }
    
    //求和数量和金额
    sum = function () {
        $scope.totalNum=0;//总数量
        $scope.totalMoney=0.00;//总金额

        for (var i = 0; i < $scope.cartList.length; i++) {
            var cart = $scope.cartList[i];
            for (var j = 0; j < cart.orderItemList.length; j++) {
                var orderItem = cart.orderItemList[j];
                $scope.totalNum += orderItem.num; //数量累加
                $scope.totalMoney += orderItem.totalFee; //总金额累加
            }

        }
    }

    //获取用户下的所有收货地址数据
    $scope.findListByUserId = function(){
        addressService.findListByUserId().success(
            function(response){
                $scope.addressList=response;
                for (var i = 0; i < response.length; i++) {
                    if(response[i].isDefault==1){ //找到默认的直接设置给$scope.address
                        $scope.address = response[i];
                        return;
                    }
                }
            }
        )
    }


    //页面选择收货地址后，将循环的addres对象传入
    $scope.selectAddress=function(address){
        $scope.address=address;
    }

    //判断是否是当前选中的地址
    $scope.isSelectedAddress=function(address){
        if(address==$scope.address){
            return true;
        }else{
            return false;
        }
    }

    $scope.order={paymentType:'1'};	//订单对象默认值 支付类型，1、微信支付，2、货到付款',

    //选择支付方式
    $scope.selectPaymentType=function(type){ //传入支付类型的值
        $scope.order.paymentType= type;
    /*    alert($scope.order.paymentType);*/
    }

    $scope.add = function(){
        //将收货地址的信息封装到order对象中进行add保存
        $scope.order.receiverAreaName=$scope.address.address;//地址
        $scope.order.receiverMobile=$scope.address.mobile;//手机号
        $scope.order.receiver=$scope.address.contact;//联系人

        orderService.add($scope.order).success(
            function(response){
                //这里测试没问题后修改为跳转到支付页面
                //alert(response.message);
                if(response.success){
                    //拷贝pay.html页面进行支付页面跳转
                    location.href="pay.html";
                }else{
                    alert(response.message);
                }
            }
        )
    }
});