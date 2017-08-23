package com.example.ahmedwahdan.flicker_photo.ui.search.GroupSearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedwahdan.flicker_photo.R;
import com.example.ahmedwahdan.flicker_photo.model.GroupItem;
import com.example.ahmedwahdan.flicker_photo.network.Routs;
import com.example.ahmedwahdan.flicker_photo.ui.search.SearchPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmedwahdan on 8/23/17.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    List<GroupItem> groupItems;
    SearchPresenter.GroupSearchPresenter presenter;
    private Context context;
    private String TAG = "GroupAdapter";

    public GroupAdapter(List<GroupItem> groupItems, Context context, SearchPresenter.GroupSearchPresenter presenter) {
        this.groupItems = groupItems;
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_name,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GroupItem item = groupItems.get(position);
        holder.groupName.setText(item.getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onGroupItemClick(item.getNsid(),item.getName());
            }
        });
        Log.d("GroupAdapter", Routs.getGroupIconUrl(item));

    }

    @Override
    public int getItemCount() {
        return groupItems.size();
    }
    public void loadMorePhoto(List<GroupItem> groupItems) {
        int currentAdapterSize = this.groupItems.size();
        this.groupItems.addAll(groupItems);
        int newAdapterSize = this.groupItems.size();
        Log.d(TAG, "loadMoreGroup: currentAdapterSize " + currentAdapterSize + " newAdapterSize " + newAdapterSize);
        notifyItemInserted(groupItems.size() );
    }

    public void updatePhotoList(List<GroupItem> groupItems) {
        this.groupItems.clear();
        this.groupItems.addAll(groupItems) ;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_group_icon)
        ImageView grouIcon;
        @BindView(R.id.item_group_name)
        TextView groupName;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.view = itemView;
        }
    }
}
