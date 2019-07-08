package lab.ourteam.newlab.Bean;
   public class listviewBean{//带图的listView
    private int imageView;
    private String name,preview;
    private int id;
    public int getId(){
        return id;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public listviewBean(int imageView, String name, int id){
        this.id=id;
        this.imageView=imageView;
        this.name=name;
        this.preview=null;
    }
       public listviewBean(int imageView, String name,String preview,int id){
           this.id=id;
           this.imageView=imageView;
           this.name=name;
           this.preview=preview;
       }

       public int getImageView() {
        return imageView;
    }
    public void setImageView(int imageView){
        this.imageView=imageView;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName(){
        return name;
    }

       public void setPreview(String preview) {
           this.preview = preview;
       }

       public String getPreview() {
           return preview;
       }
   }
