package lab.ourteam.newlab.Bean;
   public class listviewBean{
    private int imageView;
    private String text;
    private int id;
    public int getId(){
        return id;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public listviewBean(int imageView, String text, int id){
        this.id=id;
        this.imageView=imageView;
        this.text=text;
    }
    public int getImageView() {
        return imageView;
    }
    public void setImageView(int imageView){
        this.imageView=imageView;
    }
    public void setText(String text)
    {
        this.text=text;
    }
    public String getText(){
        return text;
    }
}
