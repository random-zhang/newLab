package lab.ourteam.newlab.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import lab.ourteam.newlab.Constant;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.event.MessageEvent;
import lab.ourteam.newlab.fragment.fg_center;


public class user_info_activity extends Activity {
    private ImageView user_info_portrait;
    private TextView user_info_nackName;
    private TextView user_info_phone;
    private TextView user_info_sex;
    private TextView user_info_birthday;
    private RelativeLayout user_info_portrait_team;
    private AlertDialog  portrait_selector;
    private ImageView user_info_return_menu;
    File file = null;
    File currentImageFile = null;//保存图片
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_layout);
        //EventBus.getDefault().register(this);
        initId();
        initListener();
    }
    protected  void initId(){
        user_info_portrait=findViewById(R.id.user_info_portrait);
        user_info_nackName=findViewById(R.id.user_info_username);
        user_info_phone=findViewById(R.id.user_info_userphone);
        user_info_sex=findViewById(R.id.user_info_sex);
        user_info_birthday=findViewById(R.id.user_info_birthday);
        user_info_portrait_team=findViewById(R.id.user_info_portrait_team);
        user_info_return_menu=findViewById(R.id.user_info_return_menu);
    }
    protected void initListener(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.user_info_portrait_team:{
                        setPortriatSelectorDialog().show();
                        break;
                    }
                    case R.id.user_info_return_menu:{
                        finish();
                        break;
                    }
                }
            }
        };
        user_info_portrait_team.setOnClickListener(listener);
        user_info_return_menu.setOnClickListener(listener);
    }
    public AlertDialog setPortriatSelectorDialog(){
         View portriat_selector_view;
         LayoutInflater inflater=getLayoutInflater();
         AlertDialog.Builder builder=new AlertDialog.Builder(this);
         portriat_selector_view=inflater.inflate(R.layout.portrait_selector_dialog,null,false);
         builder.setView(portriat_selector_view);
         final AlertDialog dialog=builder.create();
         TextView select_portrait_by_album=portriat_selector_view.findViewById(R.id.select_portrait_by_album);
         TextView select_portrait_by_camera=portriat_selector_view.findViewById(R.id.select_portrait_by_camera);
         TextView select_portrait_cancel=portriat_selector_view.findViewById(R.id.select_portrait_cancel);
         View.OnClickListener text_listener=new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 switch (v.getId()){
                     case R.id.select_portrait_by_album:{

                         break;
                     }
                     case R.id.select_portrait_by_camera:{
                         //调用相机
                         applyWritePermission();
                         break;
                     }
                     case R.id.select_portrait_cancel:{
                         dialog.dismiss();
                         break;
                     }
                 }
             }
         };
        select_portrait_by_album.setOnClickListener(text_listener);
        select_portrait_by_camera.setOnClickListener(text_listener);
        select_portrait_cancel.setOnClickListener(text_listener);
         return dialog;
    }
    public void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File storageDir;
        String storePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "images";
        storageDir = new File(storePath);
        storageDir.mkdirs();
        try {
            file = File.createTempFile(System.currentTimeMillis() + "", ".jpg", storageDir);
            Log.e("图片", file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = FileProvider.getUriForFile(this, "lab.ourteam.newlab", file);
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);
    }
    public void getUserInfo(){
        String userID=getIntent().getStringExtra("userID");
        if(userID!=null) {
            //sql获取服务器的用户信息


        }
            return;
    }
    public void applyWritePermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                useCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.requestPermissionRequestCode);
            }
        } else {
            useCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode == Activity.DEFAULT_KEYS_DIALER) {
            //原图片的宽度高度
            double width= user_info_portrait.getWidth();
            double height=user_info_portrait.getHeight();
            Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
            double scalew=width/bitmap.getWidth(),scaleh=height/bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.setScale((float)scalew, (float)scaleh);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
            Log.i("wechat", "压缩后图片的大小" + (bitmap.getByteCount() / 1024 / 1024)
                    + "M宽度为" + bitmap.getWidth() + "高度为" + bitmap.getHeight());
            user_info_portrait.setImageBitmap(bitmap);
            //在手机相册中显示刚拍摄的图片
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);

            //把头像数据上传到数据库
               // new postToTomcat(Constant.upload_URL).uploadPhoto(file.getAbsolutePath(), "isPortriat");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.requestPermissionRequestCode&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            useCamera();
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent= new Intent(this, fg_center.class);
            //返回更改后头像
            /*Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
            ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
            byte[] result = output.toByteArray();//转换成功了  result就是一个bit的资源数组*/
            if(file!=null) {
                HashMap map = new HashMap();
                map.put("portriat", file.getAbsolutePath());
                //把头像的地址传递给上层
                EventBus.getDefault().post(new MessageEvent(map));
                // intent.putExtra("portrait",file.getAbsolutePath());
                // startActivity(intent);
            }
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
