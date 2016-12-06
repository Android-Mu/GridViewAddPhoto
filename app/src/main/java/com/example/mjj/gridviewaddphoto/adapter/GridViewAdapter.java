package com.example.mjj.gridviewaddphoto.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mjj.gridviewaddphoto.R;

import java.util.List;

import static com.example.mjj.gridviewaddphoto.R.id.iv_delete;

/**
 * Description：图片列表适配器
 * <p>
 * Created by Mjj on 2016/12/3.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<Bitmap> data;
    private LayoutInflater inflater;

    public GridViewAdapter(Context context, List<Bitmap> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview, null);
            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.ivDelete = (ImageView) convertView.findViewById(iv_delete);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // 绑定图片原始尺寸，方便以后应用
        int[] parameter = {data.get(position).getWidth(), data.get(position).getHeight()};
        holder.imageView.setTag(parameter);
        holder.imageView.setImageBitmap(data.get(position));
        if (position == data.size() - 1) {
            holder.ivDelete.setVisibility(View.GONE);
        } else {
            holder.ivDelete.setVisibility(View.VISIBLE);
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class Holder {
        private ImageView imageView;
        private ImageView ivDelete;
    }
}
