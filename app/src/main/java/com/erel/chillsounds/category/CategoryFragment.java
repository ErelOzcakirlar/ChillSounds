package com.erel.chillsounds.category;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.erel.chillsounds.R;
import com.erel.chillsounds.service.entity.Category;
import com.erel.chillsounds.service.entity.Track;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryContract.View {

    public static final String ARG_KEY_CATEGORY = "item";

    List<Track> tracks = new ArrayList<>();
    TracksAdapter adapter = new TracksAdapter();

    CategoryContract.Presenter presenter = null;

    RecyclerView categoryRecycler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CategoryPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryRecycler = view.findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));
        categoryRecycler.setAdapter(adapter);

        presenter.requestCategory();
    }

    @Override
    public void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public Category getCategory() {
        return (Category) getArguments().getSerializable(ARG_KEY_CATEGORY);
    }

    @Override
    public void showTracks(List<Track> tracks) {
        this.tracks = tracks;
        adapter.notifyDataSetChanged();
    }

    class TracksAdapter extends RecyclerView.Adapter<TrackViewHolder>{

        @NonNull
        @Override
        public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_track, parent, false);
            return new TrackViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final TrackViewHolder holder, int position) {
            Track item = tracks.get(position);
            holder.itemName.setText(item.name);
            if(presenter.isTrackFavorite(item)){
                holder.addRemoveFavorite.setImageResource(R.drawable.ic_favorite);
            }else{
                holder.addRemoveFavorite.setImageResource(R.drawable.ic_favorite_border);
            }
            holder.addRemoveFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.favoriteForIndex(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return tracks.size();
        }
    }

    class TrackViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        ImageButton addRemoveFavorite;

        TrackViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            addRemoveFavorite = itemView.findViewById(R.id.addRemoveFavorite);
        }
    }
}
