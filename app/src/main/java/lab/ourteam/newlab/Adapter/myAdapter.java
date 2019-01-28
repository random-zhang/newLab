package lab.ourteam.newlab.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import lab.ourteam.newlab.Bean.listviewBean;
import lab.ourteam.newlab.R;

public class myAdapter extends BaseAdapter {
    private Context context;
    private List<listviewBean> list;
    private int layoutID=-1;

    public myAdapter(Context context, List<listviewBean> list,int layoutID){
        this.context=context;
        this.list=list;
        this.layoutID=layoutID;
    }
    @Override
    public int getCount(){
        if(list!=null){
            return list.size();
        }
        return 0;}
    @Override
    public Object getItem(int position){
        if(list!=null){
            return list.get(position);
        }
        return null;
    }
    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        ViewHolder hold;
        if (convertView == null) {
            hold = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(layoutID, null);
           if(layoutID==R.layout.news_item) {
               hold.imageView = (ImageView) convertView.findViewById(R.id.news_item_imageView);
               hold.nameView = (TextView) convertView.findViewById(R.id.news_item_textView);
               hold.preview=(TextView) convertView.findViewById(R.id.news_item_preview);
               hold.imageView.setImageResource(list.get(position).getImageView());
               hold.nameView.setText(list.get(position).getName());
               hold.preview.setText(list.get(position).getPreview());
           }else if(layoutID==R.layout.content_item){
               hold.imageView = (ImageView) convertView.findViewById(R.id.item_imageView);
               hold.nameView = (TextView) convertView.findViewById(R.id.item_textView);
               hold.imageView.setImageResource(list.get(position).getImageView());
               hold.nameView.setText(list.get(position).getName());
           }else if(layoutID==R.layout.friends_item){
               hold.imageView = (ImageView) convertView.findViewById(R.id.friend_item_imageView);
               hold.nameView = (TextView) convertView.findViewById(R.id.friend_item_textView);
               hold.imageView.setImageResource(list.get(position).getImageView());
               hold.nameView.setText(list.get(position).getName());
           }
            convertView.setTag(hold);
        } else {
            hold = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
    class ViewHolder {
        public ImageView imageView;
        public TextView  nameView;
        public TextView  preview;
    }
}
