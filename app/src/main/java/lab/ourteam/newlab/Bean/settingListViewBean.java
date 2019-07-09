package lab.ourteam.newlab.Bean;

public class settingListViewBean {
    private String text;
    private int imageID;
    public settingListViewBean(String text,int imageID){
        this.imageID=imageID;
        this.text=text;
    }
    public  settingListViewBean(){

    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
