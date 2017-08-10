package com.example.ahmedwahdan.flicker_photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private  Context context;
    private  List<PhotoItem> photoItems;

    public PhotoAdapter(List<PhotoItem> photoItems , Context context){
        this.photoItems = photoItems;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoItem item = photoItems.get(position);

        holder.photoText.setText(item.getTitle());
        Picasso.with(context).load(item.getGetURl()).into(holder.photoImage);

    }
    public void updateList(List<PhotoItem> photoItems){
        this.photoItems = photoItems;
        Log.d("PhotoAdapter", "photoItems.size():" + photoItems.size());

        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return photoItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo_id)
        ImageView photoImage ;
        @BindView(R.id.photo_title)
        TextView photoText;
         ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
