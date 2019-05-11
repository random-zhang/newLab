package lab.ourteam.newlab.Bean;

public class Manager {
    private String managerid;

    private String userid;

    private String subid;

    private String dmpassword;

    private String musers;

    public String getManagerid() {
        return managerid;
    }

    public void setManagerid(String managerid) {
        this.managerid = managerid == null ? null : managerid.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid == null ? null : subid.trim();
    }

    public String getDmpassword() {
        return dmpassword;
    }

    public void setDmpassword(String dmpassword) {
        this.dmpassword = dmpassword == null ? null : dmpassword.trim();
    }

    public String getMusers() {
        return musers;
    }

    public void setMusers(String musers) {
        this.musers = musers == null ? null : musers.trim();
    }
}