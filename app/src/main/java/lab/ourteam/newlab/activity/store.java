package lab.ourteam.newlab.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.Serializable;
import lab.ourteam.newlab.Bean.Bath;
import lab.ourteam.newlab.R;
public class store extends AppCompatActivity {
   private Button test3_buttton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        test3_buttton=findViewById(R.id.test3_buttton);
        test3_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(store.this) ;
//open_sound是给Preference元素设置的key
                Log.i("取值", "open_sound="+prefs.getString("axisX_setting",null));
            }
        });
    }
}
