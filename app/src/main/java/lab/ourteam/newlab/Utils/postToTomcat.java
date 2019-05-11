package lab.ourteam.newlab.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import lab.ourteam.newlab.Bean.Bath;
import lab.ourteam.newlab.Bean.Device;
import lab.ourteam.newlab.Bean.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class postToTomcat {
    public static final String USERSERVICEURI = "http://192.168.43.161:8080/lab_war_exploded/";
    private static final String BOUNDARY = UUID.randomUUID().toString();
    public static Response uploadFile(String url,File file) throws IOException {
        url+=USERSERVICEURI+url;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(getMediaType(file.getAbsolutePath()), file))
                .build();
        return okHttpClient.newCall(request).execute();
    }
    public static Response postJson(String url,String json) throws IOException {
        url=USERSERVICEURI+url;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(getMediaType(".json"), json))
                .build();
        return okHttpClient.newCall(request).execute();
    }
    private static MediaType getMediaType(String fileSuffix){
        MediaType mediaType=null;
        if (fileSuffix.contains(".jpg"))
            mediaType= MediaType.parse("image/jpg");
        else
        if (fileSuffix.contains(".png"))
            mediaType= MediaType.parse("image/png");
        else if(fileSuffix.contains(".json"))
            mediaType= MediaType.parse("application/json; charset=utf-8");
        return mediaType;
    }
    private  static  Response getResponse(String url) throws IOException{
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return  okHttpClient.newCall(request).execute();
    }
    public static File getFile(String RelativePath){//用户要取xxx文件
       // String url=USERSERVICEURI+""

          return  null;
    }
    public static File getUserFile(int userId,String fileName,String httpUrl,Context context) throws IOException {//用户要取xxx文件
        String relativeUrl=USERSERVICEURI+"file/"+httpUrl+"userId="+userId+","+"fileName";//服务器只需要知道用户名和文件名
        Response response=null;
        try {
            response=getResponse(relativeUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer=null;
        //获取response返回的文件流
        if (response==null){//图片文件放在外存储
            return null;
        }
        buffer= response.body().bytes();
        return saveToLocation.saveUserFile(buffer,fileName,context);//先保存到本地，再获取对应文件
    }
    public static Bitmap getUserPicture(int userId,String fileName,Context context) throws IOException {//获取用户拥有的图片,重要文件存在私有目录下
        //拼接url
       String httpUrl="downloadUserImage.do/";//请求的地址
       File file=getUserFile(userId,fileName,httpUrl,context);
        Bitmap bitmap = null;
        try
        {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (Exception e)
        {
           return null;
        }
        return  bitmap;
    }
    public static Bitmap getUserPortrait(Context context) throws IOException{//获取服务器保存的用户头像,
         User user=saveToLocation.getUserInfo(context);//,用户头像名称统一/用户id/images/portrait.png
         if (user==null) return  null;
         int userId=user.getUserid();
         return getUserPicture(userId,"userPortrait.png",context);//保存用户头像
    }
    public Device getDevice(int deviceId){
        return null;
    }
    public static Bath getBath(int deviceId,int subId) throws JSONException, IOException {
        Bath bath=null;
        JSONObject object=new JSONObject();
        object.put("deviceId",deviceId);
        object.put("subId",subId);

       Response response=postJson("deviceController/getDevice.json",object.toString());
        JSONObject json;
        if(response.body().string()!=null)
        json=new JSONObject(response.body().string());
        else
            return null;
        return  (Bath)json.get("bath");
    }

    }
