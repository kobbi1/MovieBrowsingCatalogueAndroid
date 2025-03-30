package com.example.moviebrowsingcatalogue.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviebrowsingcatalogue.R;
import com.example.moviebrowsingcatalogue.core.WatchlistItems;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WatchlistItemsAdapter extends RecyclerView.Adapter<WatchlistItemsAdapter.WatchlistViewHolder> {

    private List<WatchlistItems> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(WatchlistItems item);
    }

    public WatchlistItemsAdapter(List<WatchlistItems> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_watchlist, parent, false);
        return new WatchlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistViewHolder holder, int position) {
        WatchlistItems item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.type.setText(item.isTvShow() ? "TV Show" : "Movie");

        Glide.with(holder.itemView.getContext())
                .load(item.getCoverImage())
                .placeholder(R.drawable.ic_watchlist)
                .into(holder.coverImage);

        // 🔥 Handle item click
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class WatchlistViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, type;
        ImageView coverImage;

        public WatchlistViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemDescription);
            type = itemView.findViewById(R.id.itemType);
            coverImage = itemView.findViewById(R.id.itemCoverImage);
        }
    }
}
