package lab.ourteam.newlab.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import Interface.IBaseListAdapter;
import lab.ourteam.newlab.Bean.listBean;
import lab.ourteam.newlab.R;
public class listAdapter extends RecyclerView.Adapter<listAdapter.ItemViewHolder> implements IBaseListAdapter {//针对普通列表的适配器
    private List<listBean> beans;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private RecyclerView recyclerView;
    public listAdapter(Context context){
        this.mContext=context;
        //beans= CardDataUtils.getCardViewDatas();
        mInflater=LayoutInflater.from(context);
        beans=new ArrayList<>();
    }
    public void addNewItem(String text) {//添加新项
        if(beans == null) {
            beans = new ArrayList<>();
        }
        listBean bean=new listBean();
        bean.setText(text);
        beans.add(0,bean);
        ////更新数据集不是用adapter.notifyDataSetChanged()而是notifyItemInserted(position)与notifyItemRemoved(position) 否则没有动画效果。
        notifyItemInserted(0);
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

    public void deleteItem() {
        if(beans == null || beans.isEmpty()) {
            return;
        }
        beans.remove(0);
        notifyItemRemoved(0);
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout baseListView;
        private TextView baseListView_tv;
        public ItemViewHolder(View itemView) {
            super(itemView);
            baseListView=itemView.findViewById(R.id.baseListView);
            baseListView_tv=(TextView)itemView.findViewById(R.id.baseListView_tv);
        }
    }

    @Override
    public int getItemCount() {

        return beans.size();
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.baselistview,parent,false);//绑定到基础列表视图
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final int Position=position;
        //holder.baseListView.setCardBackgroundColor(mContext.getResources().getColor(R.color.DodgerBlue));
        // holder.item_cardview.setCardBackgroundColor(beans.get(position).getColor());
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
    /**
     * 适配器的点击事件接口
     */
    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
