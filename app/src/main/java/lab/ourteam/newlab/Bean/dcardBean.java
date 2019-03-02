package lab.ourteam.newlab.Bean;

import android.graphics.Bitmap;

public class dcardBean {//图片加文字
    private Bitmap bitmap;
    private String text;
    //getter setter

    public dcardBean(Bitmap bitmap,String text ){
        this.bitmap=bitmap;
        this.text=text;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getText() {
        return text;
    }


}
