package lab.ourteam.newlab.activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import lab.ourteam.newlab.R;
/*
    此页面的功能是输入指定的设备接入密码获得设备的使用权
 */
public class deviceCodeActivity extends AppCompatActivity implements Iactivity {//
    private LinearLayout deviceCode_linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_code);

    }
    @Override
    public void initID() {
        deviceCode_linearLayout=findViewById(R.id.deviceCode_linearLayout);
    }

    @Override
    public void onClick(View v) {

    }


}
