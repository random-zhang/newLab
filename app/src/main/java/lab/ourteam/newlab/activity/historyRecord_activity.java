package lab.ourteam.newlab.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import lab.ourteam.newlab.R;

public class historyRecord_activity extends AppCompatActivity implements Iactivity {
    private LinearLayout linearlayout;//打开日历表
    private TextView dateTv;
    private ListView historyRecordListview;
    private AlertDialog dialog;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historyrecord_activity);
        initID();
    }

    @Override
    public void initID() {
        linearlayout=findViewById(R.id.historyrecord_linearlayout);//更多历史记录
        dateTv=findViewById(R.id.historyrecord_datetv);//时间
        historyRecordListview=findViewById(R.id.historyrecord_listview);//记录组
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.historyrecord_linearlayout:{
               AlertDialog.Builder builder=new AlertDialog.Builder(this);
                view=getLayoutInflater().inflate(R.layout.historyrecord_dialog,null,false);
                builder.setView(view);
                builder.setCancelable(true);
                dialog=builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }
    }
    public void setDateTv(){//更新当日日期

    }

}
