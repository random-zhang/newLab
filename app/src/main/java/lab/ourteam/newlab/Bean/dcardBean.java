package lab.ourteam.newlab.Bean;

import android.graphics.Bitmap;

public class dcardBean {//图片加文字
    private int bitmap;
    private String text;
    //getter setter

    public dcardBean(int  bitmap,String text ){
        this.bitmap=bitmap;
        this.text=text;
    }

    public int  getBitmap() {
        return bitmap;
    }

    public String getText() {
        return text;
    }


}
