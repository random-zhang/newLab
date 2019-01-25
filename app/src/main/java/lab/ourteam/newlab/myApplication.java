package lab.ourteam.newlab;

import android.app.Application;
import android.net.Uri;

public class myApplication extends Application {
  private static String userName="";
 private static Uri portraitUri=null;
 public void setUserName(String userName){
     this.userName=userName;
 }
 public String getUserName(){
     return  userName;
 }
 public void  setUserInfoPortrait(Uri uri){
     portraitUri=uri;
 }

    public Uri getPortraitUri() {
        return portraitUri;
    }
}
