package lab.ourteam.newlab.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.Bean.CardViewBean;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.Utils.CardDataUtils;

public class cardViewAdapter extends RecyclerView.Adapter<cardViewAdapter.ItemCardViewHolder>{
    private List<CardViewBean> beans;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    public cardViewAdapter(Context context){
        this.mContext=context;
        beans=CardDataUtils.getCardViewDatas();
        mInflater=LayoutInflater.from(context);

    }
    public void addNewItem(String title,int color,int id) {
        if(beans == null) {
            beans = new ArrayList<>();
        }
        beans.add(0, new  CardViewBean(color,title,id));
        ////更新数据集不是用adapter.notifyDataSetChanged()而是notifyItemInserted(position)与notifyItemRemoved(position) 否则没有动画效果。
        notifyItemInserted(0);
    }
    /*public void deleteItem() {
        if(beans == null || beans.isEmpty()) {
            return;
        }
        beans.remove(0);
        notifyItemRemoved(0);
    }*/
    @Override
    public ItemCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_cardview_layout,parent,false);
        return new ItemCardViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ItemCardViewHolder holder, int position) {
        final int Position=position;
        holder.item_cardview.setCardBackgroundColor(mContext.getResources().getColor(R.color.DodgerBlue));
       // holder.item_cardview.setCardBackgroundColor(beans.get(position).getColor());
        holder.item_tv.setText(beans.get(position).getTitle());
        holder.item_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(v,Position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return beans.size();
    }

    public static class ItemCardViewHolder extends RecyclerView.ViewHolder{
        private CardView item_cardview;
        private TextView item_tv;
        public ItemCardViewHolder(View itemView) {
            super(itemView);
            item_cardview=(CardView)itemView.findViewById(R.id.item_cardview);
            item_tv=(TextView)itemView.findViewById(R.id.item_tv);
        }
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