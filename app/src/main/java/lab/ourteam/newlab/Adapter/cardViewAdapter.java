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
    public cardViewAdapter(Context context){
        this.mContext=context;
        beans=CardDataUtils.getCardViewDatas();
        mInflater=LayoutInflater.from(context);
    }
    public void addNewItem(String title,int color) {
        if(beans == null) {
            beans = new ArrayList<>();
        }
        beans.add(0, new  CardViewBean(color,title));
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
        holder.item_cardview.setCardBackgroundColor(mContext.getResources().getColor(beans.get(position).getColor()));
        holder.item_tv.setText(beans.get(position).getTitle());
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
}