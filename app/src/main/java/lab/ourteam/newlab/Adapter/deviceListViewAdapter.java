package lab.ourteam.newlab.Adapter;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.Bean.Device;
import lab.ourteam.newlab.Bean.listBean;
import lab.ourteam.newlab.R;

public class deviceListViewAdapter extends  RecyclerView.Adapter<deviceListViewAdapter.ItemViewHolder>{//设备页面
    private List<Device> beans=new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private RecyclerView recyclerView;
    private deviceListViewAdapter.OnItemClickListener onItemClickListener;
    private deviceListViewAdapter.OnItemLongClickListener onItemLongClickListener;
    public deviceListViewAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
    }
    // 当它连接到一个RecyclerView调用的方法
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }
    // 当它与RecyclerView解除连接调用的方法
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    public void addNewItem(Device device) {//添加新项
        if(beans == null) {
            beans = new ArrayList<>();
        }
        beans.add(0,device);
        ////更新数据集不是用adapter.notifyDataSetChanged()而是notifyItemInserted(position)与notifyItemRemoved(position) 否则没有动画效果。
        notifyItemInserted(0);
    }
    public void deleteItem(int position) {
        if(beans == null || beans.isEmpty()) {
            return;
        }
        beans.remove(position);
        notifyItemRemoved(position);
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout deviceItemView;
        private TextView deviceItemView_deviceName;
        private ImageView deviceItemView_deviceStatus;
        private ImageView deviceItemView_devicePiture;
        public ItemViewHolder(View itemView) {
            super(itemView);
            deviceItemView=itemView.findViewById(R.id.deviceItemView);
            deviceItemView_deviceName=(TextView)itemView.findViewById(R.id.deviceItemView_deviceName);
            deviceItemView_deviceStatus=itemView.findViewById(R.id.deviceItemView_deviceStatus);
            deviceItemView_devicePiture=itemView.findViewById(R.id.deviceItemView_devicePiture);
        }
    }
    @Override
    public int getItemCount() {
        return beans.size();
    }
    @Override
    public deviceListViewAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.device_item_view,parent,false);//绑定到基础列表视图
        return new deviceListViewAdapter.ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.deviceItemView_deviceName.setText(beans.get(position).getDevicename());
        holder.deviceItemView_devicePiture.setImageResource(beans.get(position).getPiture());
        holder.deviceItemView_devicePiture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(beans.get(position).getStatus()==1)//如果正在运行
            holder.deviceItemView_deviceStatus.setImageResource(R.mipmap.green_dot);
        else
            holder.deviceItemView_deviceStatus.setImageResource(R.mipmap.red_dot);
            holder.deviceItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(v,position);
                }
            }
        });
            holder.deviceItemView.setOnLongClickListener(new View.OnLongClickListener(){//长按事件
                @Override
                public boolean onLongClick(View v) {
                    int position = recyclerView.getChildAdapterPosition(v);
                    if(onItemLongClickListener!=null){
                        onItemLongClickListener.OnItemLongClick(v,position);
                    }
                    return true;
                }
            });
    }
    /**
     * 适配器的点击事件接口
     */
    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }
    public interface  OnItemLongClickListener{
        void OnItemLongClick(View v,int position);
    }

    public void setOnItemClickListener(deviceListViewAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnItemLongClickListener(deviceListViewAdapter.OnItemLongClickListener listener){
        this.onItemLongClickListener=listener;
    }
    public Device getDevice(int position){
        return beans.get(position);
    }
    public void updateDeviceName(int position,String text){
        Device device=beans.get(position);
        device.setDevicename(text);
        beans.set(0,device);
        notifyItemChanged(position);
    }
    public void updateViewPosition(int position){//置顶
        beans.add(0,beans.get(position));   //1  变成2 移掉2
        beans.remove(position+1);
        //notifyItemChanged(0);
        //notifyItemChanged(position);
        //notifyItemChanged();
        notifyDataSetChanged();
        //notifyItemChanged(position+1);
    }
    public void updateDevice(int position,Device device){
        beans.set(position,device);
        notifyItemChanged(position);
    }

}
