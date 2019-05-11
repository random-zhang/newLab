package lab.ourteam.newlab.Bean;

public class BathHistory {//水浴锅的历史记录
    private String subid;
    private String coordinates;
    public String getSubid() {
        return subid;
    }
    public void setSubid(String subid) {
        this.subid = subid == null ? null : subid.trim();
    }
    public String getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates == null ? null : coordinates.trim();
    }
}