package lab.ourteam.newlab.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coordinates {//坐标系
    private List<Point> coordinates;
    private double sv;
    private double st;
    public Coordinates(){
        coordinates=new ArrayList<>();
    }
    public Coordinates(Double st,Double sv,String coordinates){
        this.st=st;
        this.sv=sv;
        setCoordinates(coordinates);
    }
    public List<Point> getCoordinates() {
        return this.coordinates;
    }
    public void setCoordinates(String list) {
        if(list!=null&&!list.equals("")){//list=(x,x)(y,y)
            this.coordinates=new ArrayList<>();
            Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
            Matcher matcher = pattern.matcher(list);
            while (matcher.find()) {
                String s=matcher.group();//aa,a
                insertPoint(new Point(s));
            }
        }
        //this.list = list;
    }
    public void setCoordinates(List<Point> coordinates) {
        this.coordinates = coordinates;
    }
    public double getSv() {
        return sv;
    }
    public void setSv(double sv) {
        this.sv = sv;
    }
    public double getSt() {
        return st;
    }
    public void setSt(double st) {
        this.st = st;
    }
    public Coordinates(String coordinates){
        setCoordinates(coordinates);
    }
    public boolean insertPoint(double cv,double ct){//插入坐标
        return coordinates.add(new Point(ct,cv));
    }
    public boolean insertPoint(Point point){//插入坐标
        return coordinates.add(point);
    }
    public void clearPoint(){//插入坐标
        coordinates=new ArrayList<>();
    }
    @Override
    public String toString() {//把坐标转换成字符串
        String string="";
        for (int i=0;i<coordinates.size();i++) {
            string+=coordinates.get(i).toString();
        }
        return string;
    }

    public static  class Point implements  Serializable {
        double cv;
        double ct;
        public Point(double ct,double cv){
            this.ct=ct;
            this.cv=cv;
        }
        public Point(){
        }
        public double getCv() {
            return cv;
        }

        public void setCv(double cv) {
            this.cv = cv;
        }

        public double getCt() {
            return ct;
        }

        public void setCt(double ct) {
            this.ct = ct;
        }

        public Point(String coordinate){//n,n
            String[] s=coordinate.split(",");
            this.ct=Double.parseDouble(s[0]);
            this.cv=Double.parseDouble(s[1]);
        }
        public String toString(){
            return "("+ct+","+cv+")";
        }
    }
    public Point getCurrentPoint() {
        return (coordinates.size() > 0) ? coordinates.get(coordinates.size() - 1) : null;
    }
}