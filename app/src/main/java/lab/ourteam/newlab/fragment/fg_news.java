package lab.ourteam.newlab.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.Adapter.myAdapter;
import lab.ourteam.newlab.Bean.friendBean;
import lab.ourteam.newlab.Bean.listviewBean;
import lab.ourteam.newlab.R;

public class fg_news extends Fragment {
    private View view;
    private ListView fg_news_function_list,fg_news_friends_list;
    //private ImageView returnMenu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //view=inflater.inflate(R.layout.fg_news,container,false);
        view=inflater.inflate(R.layout.fg_news,container,false);
        initID();
        List<listviewBean> mList = new ArrayList<>();
        mList.add(new listviewBean(R.mipmap.groupchat,"通知",1));
        myAdapter adapter = new myAdapter(getContext(), mList,R.layout.friends_item);
        fg_news_function_list.setAdapter(adapter);//通讯录界面功能ListView
        createContactLists();
        return view;
    }
    public void createContactLists(){//通讯录界面好友ListView
        List<listviewBean> friends=new ArrayList<>();
        for (int i=0;i<12;i++)
            friends.add(new listviewBean(R.mipmap.groupchat,'A'+i+"",'a'+i+"",i));//模拟好友消息列表

        myAdapter adapter = new myAdapter(getContext(),friends,R.layout.news_item);
        fg_news_friends_list.setAdapter(adapter);
        //每个ListView加表头
        // TextView tvHeader=new TextView(this);
        // tvHeader.setText("城市列表头");
        //contacts_friends_list.addHeaderView(tvHeader);
    }

    public void initID(){
      //  returnMenu=(ImageView)findViewById(R.id.fg_news_return_menu);
        fg_news_function_list=view.findViewById(R.id.fg_news_function_list);
        fg_news_friends_list=view.findViewById(R.id.fg_news_friends_list);
    }
}
