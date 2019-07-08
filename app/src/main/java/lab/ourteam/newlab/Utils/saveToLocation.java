package lab.ourteam.newlab.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import lab.ourteam.newlab.Bean.User;

 public  class saveToLocation {
    // 在Internal中存储文件
    public static  void saveUserInfo(User user,Context context) {
        JSONObject obj=new JSONObject();
        try {
            obj.put("userName",user.getUsername());
            obj.put("userPhone",user.getUserphone());
            obj.put("userId",user.getUserid());
            obj.put("userPassword",user.getUserpassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FileOutputStream outputStream;
        try {
            outputStream =context.openFileOutput("userInfo.txt", Context.MODE_PRIVATE);
            outputStream.write(obj.toString().getBytes("utf-8"));
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static User getUserInfo(Context context) throws IOException {//获取用户信息
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream;
        try {
            fileInputStream =context.openFileInput("userInfo.txt");
            fileInputStream.read(buffer);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
        JSONObject obj=JSONObject.parseObject(new String(buffer));
        User user=new User();
        user.setUsername(obj.getString("userName"));
        user.setUserphone(obj.getString("userPhone"));
        user.setUserid(obj.getIntValue("userId"));
        user.setUserpassword(obj.getString("userPassword"));
        user.setUserportrait(context,obj.getIntValue("userId"));
        return user;
    }
    public static void saveUserFile(File file,String path,Context context){//把文件保存到指定路径
        FileOutputStream outputStream;
        try {
            outputStream =context.openFileOutput(path, Context.MODE_PRIVATE);
            outputStream.write(new FileInputStream(file).read());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     public static File saveUserFile(byte[] buffer,String fileName,Context context){//把文件保存到指定路径,用户专属
         FileOutputStream out;
         try {
             out =context.openFileOutput(fileName, Context.MODE_PRIVATE);
             out.write(buffer);
             out.close();
             getUserFile(fileName,context);
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
         return   getUserFile(fileName,context);
     }
     public  static  File getUserFile(String fileName,Context context){
         File file=null;
         try {
              file=new File(context.getFilesDir().toString()+File.separator+fileName);
         } catch (Exception e) {
             e.printStackTrace();
             return  null;
         }
         return file;
     }
    public static File saveUserPortrait(Bitmap bitmap) throws IOException{//覆盖方式保存用户头像
        return savePicture(bitmap,"UserPortrait.jpg");//保存用户头像
    }
    public static File savePicture(Bitmap bitmap,String fileName) throws IOException{
        String path = Environment.getExternalStorageDirectory().toString()+"/newLab/pictures/";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        File picture= new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(picture));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return picture;
    }
     public static Bitmap  getUserPortrait() throws IOException{//获取用户头像
         return getBitmap("uerPortrait.jpg");//获取用户头像
     }

     public static Bitmap getBitmap(String fileName)//获取
     {
         String path = Environment.getExternalStorageDirectory().toString()+"/newLab/pictures/"+fileName;
         Bitmap bitmap = null;
         try
         {
             File file = new File(path);
             if(file.exists())
             {
                 bitmap = BitmapFactory.decodeFile(path);
             }
         } catch (Exception e)
         {
             // TODO: handle exception
         }
         return bitmap;
     }
}
