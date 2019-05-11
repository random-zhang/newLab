package lab.ourteam.newlab.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.Bean.Device;
import lab.ourteam.newlab.Bean.listBean;
import lab.ourteam.newlab.R;

public class deviceListViewAdapter extends  RecyclerView.Adapter<deviceListViewAdapter.ItemViewHolder>{
    private List<Device> beans=new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private deviceListViewAdapter.OnItemClickListener onItemClickListener;
    public deviceListViewAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
    }
    public void addNewItem(Device device) {//添加新项
        if(beans == null) {
            beans = new ArrayList<>();
        }
        beans.add(0,device);
        ////更新数据集不是用adapter.notifyDataSetChanged()而是notifyItemInserted(position)与notifyItemRemoved(position) 否则没有动画效果。
        notifyItemInserted(0);
    }
    public void deleteItem() {
        if(beans == null || beans.isEmpty()) {
            return;
        }
        beans.remove(0);
        notifyItemRemoved(0);
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        private CardView deviceItemView;
        private TextView deviceItemView_deviceName;
        private ImageView deviceItemView_deviceStatus;
        private ImageView deviceItemView_devicePiture;
        public ItemViewHolder(View itemView) {
            super(itemView);
            deviceItemView=(CardView)itemView.findViewById(R.id.deviceItemView);
            deviceItemView_deviceName=(TextView)itemView.findViewById(R.id.baseListView_tv);
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
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final int Position=position;
        //holder.deviceItemView.setCardBackgroundColor(mContext.getResources().getColor(R.color.DodgerBlue));
        holder.deviceItemView_deviceName.setText(beans.get(position).getDevicename());
        holder.deviceItemView_devicePiture.setImageBitmap(beans.get(position).getdPiture());
        if(beans.get(position).isRunning())//如果正在运行
            holder.deviceItemView_deviceStatus.setImageResource(R.mipmap.green_dot);
        else
            holder.deviceItemView_deviceStatus.setImageResource(R.mipmap.red_dot);
            holder.deviceItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(v,Position);
                }
            }
        });
    }
    /**
     * 适配器的点击事件接口
     */
    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }
    public void setOnItemClickListener(deviceListViewAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public Device getDevice(int position){
        return beans.get(position);
    }

}
