package lab.ourteam.newlab.Bean;

import android.graphics.Bitmap;

public class friendBean {
    public String name,id,password;
    public Bitmap  portrait;
    public friendBean(){};
    public friendBean(String name,String id,String password, Bitmap  portrait){
        this.id=id;
        this.name=name;
        this.password=password;
        this.portrait=portrait;
    };

    public void setPortrait(Bitmap portrait) {
        this.portrait = portrait;
    }
    public Bitmap getPortrait() {
        return portrait;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
}
