package lab.ourteam.newlab.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.Serializable;
import lab.ourteam.newlab.Bean.Bath;
import lab.ourteam.newlab.R;
public class store extends AppCompatActivity {
   private Button testButtonUnique;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        testButtonUnique=findViewById(R.id.testButtonUnique);
        testButtonUnique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(store.this, bain_marle_activity.class);
                Bath bath=new Bath();
                bath.setSuid(10001);
                intent.putExtra("bath",(Serializable)bath);
                startActivity(intent);
            }
        });
    }
}
