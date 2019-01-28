package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.Adapter.myAdapter;
import lab.ourteam.newlab.Bean.listviewBean;
import lab.ourteam.newlab.R;

public class contacts_activity extends Activity implements Iactivity {
    private ListView contacts_function_list,contacts_friends_list;
    private ImageView returnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);
        initID();
        List<listviewBean> mList = new ArrayList<>();
        mList.add(new listviewBean(R.mipmap.groupchat,"群聊",1));
        myAdapter adapter = new myAdapter(this, mList,R.layout.friends_item);
        contacts_function_list.setAdapter(adapter);//通讯录界面功能ListView
        createContactLists();
        returnMenu.setOnClickListener(this);
    }

    public void initID(){
        returnMenu=(ImageView)findViewById(R.id.contacts_return_menu);
        contacts_function_list=findViewById(R.id.contacts_function_list);
        contacts_friends_list=findViewById(R.id.contacts_friends_list);
    }
    public void createContactLists(){//通讯录界面好友ListView
        List<listviewBean> friends=new ArrayList<>();
        LinearLayout linearLayout=findViewById(R.id.contacts_friends_linearLayout);
        for (int i=0;i<12;i++)
           friends.add(new listviewBean(R.mipmap.groupchat,('A'+i)+"",i));//模拟好友列表
         /*
         main.addView(listview);
		setContentView(main);
          */
        //
        //每个ListView加表头
        // TextView tvHeader=new TextView(this);
        // tvHeader.setText("城市列表头");
        //contacts_friends_list.addHeaderView(tvHeader);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contacts_return_menu: finish();
        }
    }
}
