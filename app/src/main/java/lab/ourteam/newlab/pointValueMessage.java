package lab.ourteam.newlab;

import java.util.List;

import lecho.lib.hellocharts.model.PointValue;

public class pointValueMessage {
    private List<PointValue> pointValueList=null;
    private int j=0;
    private int k=0;
    private boolean isDraw=false;
    public List<PointValue> getPointValueList(){
        return pointValueList;
    }
    public void setPointValueList(List<PointValue> pointValueList){
        this.pointValueList=pointValueList;
    }
    public void setJAndK(int j,int k) {
        this.j=j;
        this.k=k;
    }
    public int getJ() {
        return j;
    }
    public int getK(){
        return k;
    }
    public void setIsDraw(boolean isDraw){
        this.isDraw=isDraw;
    }
    public boolean getIsDraw(){
        return isDraw;
    }
}
