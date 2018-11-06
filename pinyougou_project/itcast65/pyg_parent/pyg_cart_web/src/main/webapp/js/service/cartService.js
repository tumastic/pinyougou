app.service('cartService',function($http){
    //购物车列表
    this.findCartList=function(){
        return $http.get('cart/findCartList.do');
    }

    //购物车增减商品数量
    this.addItemToCartList=function(itemId,num){
        return $http.get('cart/addItemToCartList.do?itemId='+itemId+'&num='+num);
    }

});