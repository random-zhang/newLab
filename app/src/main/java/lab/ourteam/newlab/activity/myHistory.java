package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import lab.ourteam.newlab.R;

public class myHistory extends Activity  implements View.OnClickListener{//我的历史记录
    private ImageView sort;//排序方式
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        initID();
        initUI();
    }
    public void initID(){
        sort=findViewById(R.id. activity_my_history_sort);
        back=findViewById(R.id. activity_my_history_return_menu);
    }
    public void initUI(){
       sort.setOnClickListener(this);
       back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id. activity_my_history_sort:{
                PopupMenu menu=new PopupMenu(myHistory.this,v);
                getMenuInflater().inflate(R.menu.my_history_sort, menu.getMenu());
                menu.setOnMenuItemClickListener(
                        new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                               switch (item.getItemId()){
                                   case R.id.myHistorySortT:{//时间排序
                                       break;
                                   }
                                   case R.id.myHistorySortC:{//设备类型排序
                                       break;
                                   }
                               }
                                return  true;
                            }
                        }
                );
                menu.show();
                break;
            }
            case R.id.activity_my_history_return_menu:{
                finish();
                break;
            }
        }
    }
}
