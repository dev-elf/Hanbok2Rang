package mountainq.organization.dongguk.hanbokapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mountainq.organization.dongguk.hanbokapp.R;
import mountainq.organization.dongguk.hanbokapp.datas.BookMark;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class BookMarkAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BookMark> items;

    public BookMarkAdapter(Context context, ArrayList<BookMark> items) {
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
        TextView nameText;
        Button delBtn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            v = View.inflate(context, R.layout.xx_bookmark_item, null);
            holder.nameText = (TextView) v.findViewById(R.id.nameText);
            holder.delBtn = (Button) v.findViewById(R.id.delBtn);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        BookMark item = items.get(position);
        holder.nameText.setText(item.getLocationName());
        return v;
    }
}
