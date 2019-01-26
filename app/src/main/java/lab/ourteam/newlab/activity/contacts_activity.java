package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
        returnMenu.setOnClickListener(this);
    }

    public void initID(){
        returnMenu=(ImageView)findViewById(R.id.contacts_return_menu);
        contacts_function_list=findViewById(R.id.contacts_function_list);
        contacts_friends_list=findViewById(R.id.contacts_friends_list);
    }
    public void createContactLists(){

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contacts_return_menu: finish();
        }
    }
}
