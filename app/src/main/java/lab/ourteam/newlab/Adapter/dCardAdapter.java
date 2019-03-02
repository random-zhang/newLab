package lab.ourteam.newlab.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lab.ourteam.newlab.Bean.CardViewBean;
import lab.ourteam.newlab.Bean.dcardBean;
import lab.ourteam.newlab.R;
import lab.ourteam.newlab.Utils.CardDataUtils;
import lab.ourteam.newlab.Utils.dCardDataUtils;

public class dCardAdapter extends RecyclerView.Adapter<dCardAdapter .itemCardViewHolder>{
    private List<dcardBean> beans;
    private LayoutInflater mInflater;
    private Context mContext;
    public dCardAdapter(Context context){
        this.mContext=context;
        beans=dCardDataUtils.getCardViewDatas();
        mInflater=LayoutInflater.from(context);
    }
    public  void addNewItem(Bitmap bitmap,String text) {
        if(beans == null) {
            beans = new ArrayList<>();
        }
        beans.add(0, new  dcardBean(bitmap,text));
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
    @Override   //创建视图并返回一个匹配的viewholder
    public itemCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.dcarditem,parent,false);
        return new itemCardViewHolder(view);
    }
    @Override   //用每个item数据来填充viewholder
    public void onBindViewHolder(itemCardViewHolder holder, int position) {
       // holder.item_cardview.setCardBackgroundColor(mContext.getResources().getColor(beans.get(position).getColor()));
        holder.item_tv.setText(beans.get(position).getText());
        holder.item_imageView.setImageBitmap(beans.get(position).getBitmap());
    }
    @Override   //返回item的数目
    public int getItemCount() {
        return beans.size();
    }

    public static class itemCardViewHolder extends RecyclerView.ViewHolder{
        private CardView item_cardview;
        private TextView item_tv;
        private ImageView item_imageView;
        public itemCardViewHolder(View itemView) {
            super(itemView);
            item_cardview=(CardView)itemView.findViewById(R.id.dCardView);
            item_tv=(TextView)itemView.findViewById(R.id.dcarditem_textView);
            item_imageView=(ImageView)itemView.findViewById(R.id.dcarditem_imageView);
        }
    }
}