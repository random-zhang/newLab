package lab.ourteam.newlab.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import Interface.IBaseListAdapter;
import lab.ourteam.newlab.Bean.settingListViewBean;
import lab.ourteam.newlab.R;
public class settingListViewAdapter extends RecyclerView.Adapter<settingListViewAdapter.ItemViewHolder> implements IBaseListAdapter {
    private List<settingListViewBean> beans;
    private LayoutInflater mInflater;
    private Context mContext;
    private settingListViewAdapter.OnItemClickListener onItemClickListener;
    private RecyclerView recyclerView;
    public settingListViewAdapter(Context context,List<settingListViewBean> beans){
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
        this.beans=beans;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.arrow_list_item,viewGroup,false);//绑定到设置列表视图
        return new settingListViewAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder  holder, int position) {
        holder.baseListView_tv.setText(beans.get(position).getText());
        holder.baseListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(v,position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return beans.size();
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
     public   static class ItemViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout baseListView;
        private TextView baseListView_tv;
        public ItemViewHolder(View itemView) {
            super(itemView);
            baseListView=itemView.findViewById(R.id.arrow_list_item);
            baseListView_tv=(TextView)itemView.findViewById(R.id.arrow_list_item_tv);
        }
    }
    /**
     * 适配器的点击事件接口
     */
    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }
    public void setOnItemClickListener(settingListViewAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
