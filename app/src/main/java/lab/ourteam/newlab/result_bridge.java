package lab.ourteam.newlab;

/**
 * 該類用於傳值回MainActivity
 */
public class result_bridge {
    private boolean isAppoint=true;
    private boolean isSeries_sv=false;
    private long time_remaining=0;
    private boolean hint=false;
    private int  series_sv;
    public void setTime_remaining(long time_remaining){ this.time_remaining=time_remaining; }
    public long getTime_remaining(){ return time_remaining; }
    public boolean getIsAppoint(){ return isAppoint; }
    public void setAppoint(boolean isAppoint){ this.isAppoint=isAppoint; }
    public void setHint(boolean hint){ this.hint=hint;}
    public boolean getHint(){return hint;}
    public int getSeries_sv() { return series_sv; }
    public void setSeries_sv(int series_sv) { this.series_sv = series_sv; }
    public boolean getIsSeries_sv() { return isSeries_sv; }
    public void setisSeries_sv(boolean isSeries_sv) { this.isSeries_sv = isSeries_sv; }
}