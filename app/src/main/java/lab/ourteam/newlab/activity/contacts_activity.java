package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
       // TextView tvHeader=new TextView(this);
       // tvHeader.setText("城市列表头");
        //contacts_friends_list.addHeaderView(tvHeader);
        List<listviewBean> mList = new ArrayList<>();
        mList.add(new listviewBean(R.mipmap.groupChat,"群聊",1));
        myAdapter adapter = new myAdapter(this, mList);
        contacts_function_list.setAdapter(adapter);//通讯录界面功能ListView
        returnMenu.setOnClickListener(this);
    }

    public void initID(){
        returnMenu=(ImageView)findViewById(R.id.contacts_return_menu);
        contacts_function_list=findViewById(R.id.contacts_function_list);
        contacts_friends_list=findViewById(R.id.contacts_friends_list);
    }
    public void createContactLists(){//通讯录界面好友ListView

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contacts_return_menu: finish();
        }
    }
}
