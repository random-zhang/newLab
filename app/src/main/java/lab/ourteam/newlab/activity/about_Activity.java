package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import lab.ourteam.newlab.R;

public class about_Activity extends  Activity {
    private ImageView returnMenu;
    private Button  feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        feedback=(Button)findViewById(R.id.about_feedback);
        feedback.setOnClickListener(new ButtonListener());
        returnMenu=(ImageView)findViewById(R.id.about_return_menu);
        returnMenu.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }}); }
private class ButtonListener implements View.OnClickListener {
        @Override
    public void onClick(View arg0){
            switch(arg0.getId()){
                case R.id.about_feedback:{
                   Uri uri=Uri.parse("mailto:2586814380@qq.com");
                   Intent intent=new Intent(Intent.ACTION_SENDTO,uri);
                   startActivity(intent);
                    break;
                }
                //可拓展
            }
        }
}

}

