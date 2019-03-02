package lab.ourteam.newlab.Bean;

import java.io.Serializable;

public class CardViewBean implements Serializable {//纯文字
    private int color;
    private String title;
    private int id;
    public CardViewBean(int color,String text,int id){
        this.color=color;
        this.title=text;
        this.id=id;
    }
    public int getColor() {
        return color;
    }
    public String getTitle(){
        return title;
    }
    public int getId(){
        return id;
    }
}
