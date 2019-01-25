package lab.ourteam.newlab.Bean;

public class CardViewBean {
    private int color;
    private String title;
    public CardViewBean(int color,String text){
        this.color=color;
        this.title=text;
    }
    public int getColor() {
        return color;
    }
    public String getTitle(){
        return title;
    }
}
