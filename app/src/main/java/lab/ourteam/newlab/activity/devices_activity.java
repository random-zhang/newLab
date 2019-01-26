package lab.ourteam.newlab.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import lab.ourteam.newlab.Presenter.loginPresenter;
import lab.ourteam.newlab.R;

public class devices_activity extends Activity {
    private ImageView returnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        returnMenu.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});
    }
}
