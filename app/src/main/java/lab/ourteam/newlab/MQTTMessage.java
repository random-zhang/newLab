package lab.ourteam.newlab;

public class MQTTMessage {
     private String message;
     private int total_second=0;//默认为零
     public String getMessage(){
        return message;
    }
     public void setMessage(String message){
        this.message=message;
    };
     public int getTotal(){ return total_second; }
     public void setTotal(int total_second){this.total_second=total_second;}

}
