package JU4C;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

public class test {
    /**
     * 测试无参数函数mock
     */
    @Test
    public void testReturnDirect()
    {
        String mocked = "mocked Return";
        Demo demo  = Mockito.mock(Demo.class);
        Mockito.when(demo.methodNoParameters()).thenReturn(mocked);
        Assert.assertEquals(demo.methodNoParameters(), mocked);
    }

    /**
     * 测试任意基本类型参数函数mock
     */
    @Test
    public void testMethodWithParameter()
    {
        String word= "mocked Return";
        Demo demo =  Mockito.mock(Demo.class);
        Mockito.when(demo.speak(Mockito.anyString())).thenReturn(word);
        Assert.assertEquals(demo.speak("你好"), word);
    }

    /**
     * 测试特定参数mock
     */
    @Test
    public void testMethodWithSpecificParameter()
    {
        String word= "mocked Return";
        Demo demo =  Mockito.mock(Demo.class);
        Mockito.when(demo.speak(Mockito.matches(".*大爷$"))).thenReturn(word);
        Assert.assertEquals(demo.speak("隔壁李大爷"), word);
    }

    /**
     * 测试自定义类型参数的mock
     */
    @Test
    public void testMethodWithCustomParameter()
    {
        String word= "mocked Return";
        Demo demo =  Mockito.mock(Demo.class);
        Mockito.when(demo.methodCustomParameters(Mockito.argThat(new IsParameterClass()),
                Mockito.anyString())).thenReturn(word);
        Assert.assertEquals(demo.methodCustomParameters(new ParameterClass(), "你猜"), word);
    }
    //自定义一个与之匹配的matcher类
    /*
    * ArgumentMatcher抽象类
自定义参数匹配器的时候需要继承ArgumentMatcher抽象类，它实现了Hamcrest框架的Matcher接口，定义了describeTo方法，
所以我们只需要实现matches方法在其中定义规则即可。
   下面自定义的参数匹配器是匹配类型为ParameterClass.class的para参数
    * */
    class IsParameterClass extends ArgumentMatcher<ParameterClass> {
        public boolean matches(Object para) {
            return para.getClass() == ParameterClass.class;
        }
    }

    /**
     * 测试mock的函数返回null
     */
    @Test
    public void testMethodWithReturnNull()
    {
        String word= "mocked Return";
        Demo demo =  Mockito.mock(Demo.class);
        Mockito.when(demo.speak(Mockito.anyString())).thenReturn(null);
        Assert.assertNotEquals(demo.speak("你猜"), word);  //assertNotEquals  不等于
    }

    /**
     * 构造mock的函数抛出异常，
     * 可以在testng中设置expectedExceptions以显示声明会抛出指定类型的异常
     * 测试mock的函数抛出异常
     */
    @Test(expected=org.mockito.exceptions.base.MockitoException.class)
    public void testMethodReturnException()
    {
        String word= "mocked Return";
        Demo demo =  Mockito.mock(Demo.class);
        Mockito.when(demo.speak(Mockito.anyString())).thenThrow(new Exception());
        demo.speak("你猜");
    }

    /**
     * 测试mock的不同次调用返回不同的值
     */
    @Test
    public void testMethodMultiDiffReturn()
    {
        String word= "mocked Return 0";
        String word1= "mocked Return 1";
        Demo demo =  Mockito.mock(Demo.class);
        Mockito.when(demo.speak(Mockito.anyString())).thenReturn(word).thenReturn(word1);
        Assert.assertEquals(demo.speak("你猜"), word);
        Assert.assertEquals(demo.speak("你猜"), word1);
    }

    /**
     * verify()验证方法是否被mock且是否为所执行的参数调用
     */
    @Test(expected= org.mockito.exceptions.misusing.NotAMockException.class)
    public void testMockedMethodRun() {
        String word = "mocked Return";
        Demo demo = Mockito.mock(Demo.class);
        Mockito.when(demo.speak(Mockito.anyString())).thenReturn(word);
        Assert.assertEquals(demo.speak("你猜"), word);
        Mockito.verify(demo).speak("你猜");
        //下面这个参数的方法调用并没有被执行过，所以会抛出NotAMockException的异常
        Mockito.verify(demo).speak("nicai");

    }

}
