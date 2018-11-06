import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class httpclient_get {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.baidu.com/");
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() ==200){
                String content = EntityUtils.toString(response.getEntity(),"UTF-8");
                System.out.println("内容长度"+content.length());
               // System.out.println(content);//得到的是网页的源代码
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(response!=null){
                response.close();
            }
            httpClient.close();
        }
    }
}
