package lab.ourteam.newlab.Utils;

import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.Bean.dcardBean;
import lab.ourteam.newlab.R;

public class dCardDataUtils {//提供所有本软件支持的设备

    dCardDataUtils(){

    }
    public static  List<dcardBean> getCardViewDatas(){
        List<dcardBean> list=new ArrayList<>() ;
        list.add(new dcardBean(R.mipmap.bain_marle,"水浴锅"));
        list.add(new dcardBean(R.mipmap.stirrer,"电动搅拌器"));
        return list;
    }

}
