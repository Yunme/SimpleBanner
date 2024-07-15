package me.ryan.banner.core;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.ryan.banner.R;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private List<String> data = new ArrayList<>();

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        String url = data.get(getUIPosition(position));
        if (holder.imageView == null) return;
        Glide.with(holder.imageView).load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (data.size() < 2) return data.size();
        return getRealItemCount() + 2;
    }

    public int getRealItemCount() {
        return data.size();
    }

    public int getUIPosition(int position) {
        int realItemCount = getRealItemCount();
        if (position == 0) return realItemCount - 1;
        if (position == realItemCount + 1) return 0;
        return position - 1;
    }

    public void setData(List<String> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView instanceof ImageView) {
                imageView = (ImageView) itemView;
            }
        }
    }
}
