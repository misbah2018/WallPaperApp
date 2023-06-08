package com.example.videostraming;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder> {
    private List<String> imageUrlList;
    private Context context;
    private String selectedImageUrl;

    public WallpaperAdapter(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_wallpaper, parent, false);
        return new WallpaperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, int position) {
        String imageUrl = imageUrlList.get(position);
        holder.bind(imageUrl);

        // Set click listener to select/deselect the image
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUrl == null) {
                    selectedImageUrl = imageUrl;
                    holder.itemView.setBackgroundColor(Color.LTGRAY);
                } else if (selectedImageUrl.equals(imageUrl)) {
                    selectedImageUrl = null;
                    holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    // Deselect the previously selected image
                    int selectedIndex = imageUrlList.indexOf(selectedImageUrl);
                    notifyItemChanged(selectedIndex);
                    selectedImageUrl = imageUrl;
                    holder.itemView.setBackgroundColor(Color.LTGRAY);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }

    public String getSelectedImageUrl() {
        return selectedImageUrl;
    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder {
        private ImageView wallpaperImageView;

        public WallpaperViewHolder(@NonNull View itemView) {
            super(itemView);
            wallpaperImageView = itemView.findViewById(R.id.wallpaperRecyclerImageView);
        }

        public void bind(String imageUrl) {
            // Use Glide library to load the image from the URL into the ImageView
            Glide.with(context).load(imageUrl).into(wallpaperImageView);
        }
    }
}
