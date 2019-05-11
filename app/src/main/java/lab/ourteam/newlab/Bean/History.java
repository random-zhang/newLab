package lab.ourteam.newlab.Bean;

public class History {//历史记录
    private String historyid;//历史记录的编号
    private Long subid;

    public String getHistoryid() {
        return historyid;
    }

    public void setHistoryid(String historyid) {
        this.historyid = historyid == null ? null : historyid.trim();
    }

    public Long getSubid() {
        return subid;
    }

    public void setSubid(Long subid) {
        this.subid = subid;
    }
}