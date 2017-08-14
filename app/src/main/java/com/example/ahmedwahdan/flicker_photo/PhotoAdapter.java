package com.example.ahmedwahdan.flicker_photo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PhotoAdapter.class.getSimpleName();
    private final int VIEW_ITEM = 1;
    private final int VIEW_Divide = 0;
    private  Context context;
    private  List<PhotoItem> photoItems;
    private int page = 1;

    PhotoAdapter(List<PhotoItem> photoItems, Context context) {
        this.photoItems = photoItems;
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            Log.d(TAG, "onCreateViewHolder: " + "VIEW_ITEM");

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
            return new ViewHolder(v);
        } else if (viewType == VIEW_Divide) {
            Log.d(TAG, "onCreateViewHolder: " + "VIEW_Divide");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.divider_page_item, parent, false);
            return new DividerNumberViewHolder(v);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder && position < photoItems.size()) {
            PhotoItem item = photoItems.get(position);
            Log.d(TAG, item.getGetURl());
            Picasso.with(context)
                    .load(item.getGetURl())
                    .placeholder(R.drawable.progress_animation)
                    .into(((ViewHolder) holder).photoImage);
        } else {

        }

    }

    @Override
    public int getItemViewType(int position) {
//         if( position % 20 == 0 && position > 0){
//             Log.d(TAG, "getItemViewType: " +"VIEW_PROG");
//             page ++;
//             return VIEW_PROG;
//         }else {
        Log.d(TAG, "getItemViewType: " + "VIEW_ITEM");

        return VIEW_ITEM;
        //     }

    }

    void updatePhotoList(List<PhotoItem> photoItems) {
         this.photoItems.clear();
         this.photoItems.addAll(photoItems) ;
         notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        int count = (photoItems.size() + page);
        Log.d(TAG, "count:" + count);
        return count;
    }

    public void loadMorePhoto(List<PhotoItem> photosList) {
        int currentAdapterSize = this.photoItems.size();
        this.photoItems.addAll(photosList);
        int newAdapterSize = this.photoItems.size();
        Log.d(TAG, "loadMorePhoto: currentAdapterSize " + currentAdapterSize + " newAdapterSize " + newAdapterSize);
        notifyItemInserted(photoItems.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo_id)
        @NonNull
        ImageView photoImage ;
         ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

         }
    }

    public static class DividerNumberViewHolder extends RecyclerView.ViewHolder {
        public DividerNumberViewHolder(View itemView) {
            super(itemView);
        }
    }
}
