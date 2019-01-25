package lab.ourteam.newlab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class myFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int number=3;
    private List<Fragment> mFragmentList;
    public myFragmentPagerAdapter(FragmentManager manager, List<Fragment>mFragmentList){
        super(manager);
        this.mFragmentList=mFragmentList;
    }
    @Override
    public int getCount(){
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position){
        return  mFragmentList.get(position);
    }
}
