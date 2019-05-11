package lab.ourteam.newlab.Model;

import android.util.Log;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import Configure.ServerConfiguration;
import lab.ourteam.newlab.Bean.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class UserInfoModel {
    /**
     * 获取用户详细信息
     *
     */
    public User getUserInfo(int userID) throws Exception
    {
        User user = null;
        HttpClient httpclient = new DefaultHttpClient();
        List<NameValuePair> qparams = new ArrayList<>();
    //    HttpResponse response = httpclient.execute(httpget);

        httpclient.getConnectionManager().shutdown();
        return user;
    }
}
