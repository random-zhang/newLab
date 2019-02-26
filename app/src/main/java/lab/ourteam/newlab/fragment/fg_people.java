package lab.ourteam.newlab.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import lab.ourteam.newlab.R;
import lab.ourteam.newlab.activity.Iactivity;
import lab.ourteam.newlab.activity.qrcode_activity;

public class fg_people extends Fragment implements Iactivity{//测试页面
    private Button testButton;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.fg_people,container,false);
        initID();
        testButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void initID() {
        testButton=view.findViewById(R.id.testButton);
    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.testButton:{
            new IntentIntegrator(getActivity()).setCaptureActivity(qrcode_activity.class).initiateScan();
        }
    }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result !=null) {
            if(result.getContents() ==null ) {
                Log.d(getClass().getName(), "Cancelled");
                Toast.makeText(getContext(),"扫描结果为空",Toast.LENGTH_LONG).show();
            } else {
                Log.d(getClass().getName(), "Scanned: " + result.getContents());
                Toast.makeText(getContext(), result.getContents(),Toast.LENGTH_LONG).show();
            }
        }
    }

}
