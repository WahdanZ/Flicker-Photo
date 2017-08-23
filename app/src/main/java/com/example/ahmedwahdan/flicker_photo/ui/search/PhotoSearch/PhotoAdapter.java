package com.example.ahmedwahdan.flicker_photo.ui.search.PhotoSearch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedwahdan.flicker_photo.R;
import com.example.ahmedwahdan.flicker_photo.helper.FileHelper;
import com.example.ahmedwahdan.flicker_photo.helper.PhotoState;
import com.example.ahmedwahdan.flicker_photo.model.PhotoItem;
import com.example.ahmedwahdan.flicker_photo.ui.search.MVPViewer;
import com.example.ahmedwahdan.flicker_photo.ui.search.SearchPresenter;
import com.example.ahmedwahdan.flicker_photo.utils.Const;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmedwahdan on 8/10/17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Listener listener;

    private static final String TAG = PhotoAdapter.class.getSimpleName();
    private SearchPresenter.PhotoSearchPresenter  presenter;
    public HashMap<String, PhotoState > picStatusList = new HashMap<>();
    public final static int VIEW_ITEM = 1;
    public  final static int VIEW_DIVIDE = 0;
    private  Context context;
    protected   List<PhotoItem> photoItems;
    private int page = 1;

    PhotoAdapter(List<PhotoItem> photoItems, Context context , SearchPresenter.PhotoSearchPresenter presenter ) {
        this.photoItems = photoItems;
        this.context = context;
        this.presenter = presenter;
      //  this.listener =listener;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder ) {
            final PhotoItem item = photoItems.get(position);
            final String photoID = FileHelper.getFlickrFilename(item);
            if (!FileHelper.isFileFound(item)){
                picStatusList.put(item.getId(),PhotoState.PIC_STATUS_LOADING);
                presenter.downloadPhoto(item, new MVPViewer.PhotoAdapterView() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap) {
                        picStatusList.put(photoID,PhotoState.PIC_STATUS_SAVED);
                        if (bitmap != null){
                           // updatePhotoItem(bitmap, (ViewHolder) holder, item);
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notifyItemChanged(position);
                                }});

                        }
            }
                @Override
                public void onBitmapFailed() {
                    picStatusList.put(photoID,PhotoState.PIC_STATUS_FAIL);
                }
            });
            }
            else {
                picStatusList.put(photoID,PhotoState.PIC_STATUS_SAVED);
                loadImageFromDisk((ViewHolder) holder, item);
            }



            if(picStatusList.get(photoID) == PhotoState.PIC_STATUS_LOADING){
                Picasso.with(context)
                        .load(R.drawable.progress_animation)
                        .tag(context)
                        .into(((ViewHolder) holder).photoImage);
            }



//                Log.d(TAG, item.getGetURl());
//            Picasso.with(context)
//                    .load(NetworkUtils.getPhotoUrl(item))
//                    .fit()
//                    .centerCrop()
//                    .tag(context)
//                    .placeholder(R.drawable.progress_animation)
//                    .into(((ViewHolder) holder).photoImage);
        }
        else if (holder instanceof DividerNumberViewHolder){
            ((DividerNumberViewHolder) holder).pageNumber.setText(""+((position+1)/Const.MAX_NUMBER_PER_REQUEST));

        }

    }

    private void updatePhotoItem(final Bitmap bitmap, final ViewHolder holder, final PhotoItem item) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Code for the UiThread
                holder.photoImage.setImageBitmap(bitmap);
                holder.photoImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.onPhotoItemClicked(FileHelper.getFlickrFilePath(item));
                    }
                });
            }
        });
    }

    private void loadImageFromDisk(ViewHolder holder, PhotoItem item) {
        final File imageFile = new File(FileHelper.getDefaultSaveFile(), FileHelper.getFlickrFilename(item));
        Log.d(TAG, "imageFile.isFile():" + imageFile.isFile());
        Picasso.with(context)
           .load(imageFile)
           .fit().centerCrop()
           .tag(context)
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
           .placeholder(R.drawable.progress_animation)
           .into(holder.photoImage);
         holder.photoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onPhotoItemClicked(imageFile.getAbsolutePath());
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return ( position % Const.MAX_NUMBER_PER_REQUEST == 0 && position > 0) ? VIEW_DIVIDE : VIEW_ITEM;


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
    public interface Listener {
        void onImageClicked(View view , String imagePath);
    }
}
