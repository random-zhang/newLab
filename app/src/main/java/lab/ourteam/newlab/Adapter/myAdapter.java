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
    public myAdapter(Context context, List<listviewBean> list){
        this.context=context;
        this.list=list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.content_item, null);
            convertView.setTag(hold);
        } else {
            hold = (ViewHolder) convertView.getTag();
        }
        hold.imageView = (ImageView) convertView.findViewById(R.id.item_imageView);
        hold.textView = (TextView) convertView.findViewById(R.id.item_textView);
        hold.imageView.setImageResource(list.get(position).getImageView());
        hold.textView.setText(list.get(position).getText());
        return convertView;
    }
    class ViewHolder {
        public ImageView imageView;
        public TextView  textView;
    }
}
