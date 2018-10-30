package JU4C;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.*;


public class myTest {
    @Test
    public void myTest() {
        /* 创建 Mock 对象 */
        List list = mock(List.class);
        /* 设置预期，当调用 get(0) 方法时返回 "111"  该方法就是Stub*/
        when(list.get(0)).thenReturn("111");
       /*
           常用断言介绍: 1. assertEquals([String message],Object target,Object result)
            target与result不相等，中断测试方法，输出message
            assertEquals(a, b) 测试a是否等于b（a和b是原始类型数值(primitive value)或者必须为实现比较而具有equal方法）
            assertEquals断言两个对象相等，若不满足，方法抛出带有相应信息
        */
        Assert.assertEquals("asd", 1, 1);
        /* 设置后返回期望的结果 */
        System.out.println(list.get(0));
        /* 没有设置则返回 null */
        System.out.println(list.get(1));

        /* 对 Mock 对象设置无效 */
        list.add("12");
        list.add("123");
        /* 返回之前设置的结果 */
        System.out.println(list.get(0));

        /* 返回 null */
        System.out.println(list.get(1));

        /* size 大小为 0 */
        System.out.println(list.size());

        /* 验证操作，验证 get(0) 调用了 2 次 */
        /*该方法验证 get(0) 方法调用的次数 verify(list, times(2)).get(0);，还可以设置是否调用过，调用时间等等。
        * 超过设置的值就会报错
        * */
        verify(list, times(2)).get(0);

        /* 验证返回结果 */
        String ret = (String)list.get(0);
        Assert.assertEquals(ret, "111");
        /*
        * 如果返回预期结果不同,程序会将错误打印出来
        * */
    }



}
