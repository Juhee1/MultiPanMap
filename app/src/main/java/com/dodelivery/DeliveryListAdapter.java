package com.dodelivery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dodelivery.core.DeliveryDetails;

import java.util.List;

/**
 * Created by juhee on 28/3/17.
 */

public class DeliveryListAdapter extends ArrayAdapter<DeliveryDetails> {

    private Context context = null;
    private List<DeliveryDetails> items;

    public DeliveryListAdapter(Context context, List<DeliveryDetails> items) {
        super(context, R.layout.delivery_detail_row, items);
        this.context = context;
        this.items = items;
    }

    static class ViewHolder{
        TextView tvPlace;
        ImageView imgView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.delivery_detail_row, null);
            holder.tvPlace = (TextView) view.findViewById(R.id.text_deliver_to);
            holder.imgView = (ImageView) view.findViewById(R.id.icon_delivery);
            holder.imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        DeliveryDetails item = getItem(position);
        holder.tvPlace.setText(items.get(position).getDescription());
        int id = context.getResources().getIdentifier(item.getImageUrl(), "drawable", context.getPackageName());
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), id);
        holder.imgView.setImageBitmap(bm);
        return view;
    }
}