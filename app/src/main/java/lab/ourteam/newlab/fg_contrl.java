package lab.ourteam.newlab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lab.ourteam.newlab.Adapter.cardViewAdapter;

public class fg_contrl extends Fragment implements View.OnClickListener{
    private View view;
    private RecyclerView recycler_cardview;
    private ImageView add_device;
    private   cardViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.fg_contrl,container,false);
        recycler_cardview=view.findViewById(R.id.id_recyclerView);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_cardview.setLayoutManager(linearLayoutManager);
         adapter=new cardViewAdapter(getContext());
        recycler_cardview.setAdapter(adapter);
        add_device=getActivity().findViewById(R.id.add_device);
        add_device.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id. add_device) {
            adapter.addNewItem("新设备",R.color.cardView);
            linearLayoutManager.scrollToPosition(0);
        }
    }
}
