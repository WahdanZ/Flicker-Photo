package com.example.ahmedwahdan.flicker_photo.ui.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedwahdan.flicker_photo.R;
import com.example.ahmedwahdan.flicker_photo.network.model.PhotoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PhotoAdapter.class.getSimpleName();
    public final static int VIEW_ITEM = 1;
    public  final static int VIEW_DIVIDE = 0;
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
            return new ViewHolder(v);
        } else if (viewType == VIEW_DIVIDE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.divider_page_item, parent, false);
            return new DividerNumberViewHolder(v);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder ) {
            PhotoItem item = photoItems.get(position);
            Log.d(TAG, item.getGetURl());
            Picasso.with(context)
                    .load(item.getGetURl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .into(((ViewHolder) holder).photoImage);
        }
        else if (holder instanceof DividerNumberViewHolder){
            ((DividerNumberViewHolder) holder).pageNumber.setText(""+(position/21));

        }

    }

    @Override
    public int getItemViewType(int position) {
        return ( position % 21 == 0 && position > 0) ? VIEW_DIVIDE : VIEW_ITEM;


    }

    void updatePhotoList(List<PhotoItem> photoItems) {
         this.photoItems.clear();
         this.photoItems.addAll(photoItems) ;
         notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return photoItems.size() ;
    }

    public void loadMorePhoto(List<PhotoItem> photosList) {
        int currentAdapterSize = this.photoItems.size();
        this.photoItems.addAll(photosList);
        int newAdapterSize = this.photoItems.size();
        Log.d(TAG, "loadMorePhoto: currentAdapterSize " + currentAdapterSize + " newAdapterSize " + newAdapterSize);
        page++;
        notifyItemInserted(photoItems.size() +page);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo_id)
        ImageView photoImage ;
         ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

         }
    }

    public static class DividerNumberViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.page_number)
        TextView pageNumber;
        public DividerNumberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
