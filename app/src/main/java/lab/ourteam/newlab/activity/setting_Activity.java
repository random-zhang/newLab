package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import lab.ourteam.newlab.R;

public class setting_Activity extends Activity {
    private ImageView returnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        Intent intent = getIntent();
        returnMenu=(ImageView)findViewById(R.id.setting_return_menu);
        returnMenu.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});
    }
}
