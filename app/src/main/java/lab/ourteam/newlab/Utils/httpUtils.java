package lab.ourteam.newlab.Utils;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import Configure.ServerConfiguration;

public class httpUtils {
    public static final String IP = "192.168.0.108";//服务器地址
    public static final int PORT = 8080;//要根据应用服务器tomcat的端口改变
    public static final String USERSERVICEURI = "http://localhost:8080/lab_war_exploded/";
    public static  HttpResponse getResponse(String subUri,JSONObject json) throws IOException, URISyntaxException {
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> qparams = new ArrayList<>();
        String uri="http://"+ServerConfiguration.IP+":"+ ServerConfiguration.PORT+"/"+ServerConfiguration.USERSERVICEURI+"/"+subUri;
        HttpPost httpPost=new HttpPost();
        StringEntity s = new StringEntity(json.toString());
        s.setContentEncoding("UTF-8");
        s.setContentType("application/json");//发送json数据需要设置contentType
        httpPost.setEntity(s);
        return httpclient.execute(httpPost);
    }

}
