package lab.ourteam.newlab.Bean;

public class Bath extends Device  {


    private Double sv;//设定温度

    private Double cv;//当前温度

    private Double st;//设定时间

    private Double ct;//当前时间

    private String coordinates;

    public Double getSv() {
        return sv;
    }

    public void setSv(Double sv) {
        this.sv = sv;
    }

    public Double getCv() {
        return cv;
    }

    public void setCv(Double cv) {
        this.cv = cv;
    }

    public Double getSt() {
        return st;
    }

    public void setSt(Double st) {
        this.st = st;
    }

    public Double getCt() {
        return ct;
    }

    public void setCt(Double ct) {
        this.ct = ct;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates == null ? null : coordinates.trim();
    }
}
