package mountainq.organization.dongguk.hanbokapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mountainq.organization.dongguk.hanbokapp.R;
import mountainq.organization.dongguk.hanbokapp.datas.AA_StaticDatas;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class APIAdapter extends BaseAdapter{
    private AA_StaticDatas mData = AA_StaticDatas.getInstance();
    private Context context;
    private ArrayList<String> items;

    public APIAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        if(items != null) return items.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        if(items != null) return items.get(position);
        else return null;
    }

    public void deleteItem(int position){
        items.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        LinearLayout itemBg;
        TextView index;
        TextView text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
       ViewHolder holder = new ViewHolder();
        if(convertView == null){
            v = View.inflate(context, R.layout.xx_api_item, null);
            holder.itemBg = (LinearLayout) v.findViewById(R.id.itemBg);
            holder.index = (TextView) v.findViewById(R.id.idx);
            holder.text = (TextView) v.findViewById(R.id.nameText);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mData.getHeight()/10);
        holder.itemBg.setLayoutParams(llp);
        holder.itemBg.setPadding(10, 20, 10, 20);
        holder.itemBg.setBackgroundColor(Color.TRANSPARENT);
        holder.itemBg.setGravity(Gravity.CENTER);
        holder.index.setText(String.valueOf(position+1));
        String item = items.get(position);
        holder.text.setText(item);
        holder.text.setTextSize(15);
        return v;
    }
}
