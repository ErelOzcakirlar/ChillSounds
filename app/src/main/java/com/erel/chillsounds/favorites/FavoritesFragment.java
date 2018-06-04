package com.erel.chillsounds.favorites;

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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.erel.chillsounds.MainActivity;
import com.erel.chillsounds.R;
import com.erel.chillsounds.service.RSWebService;
import com.erel.chillsounds.service.ServiceResponse;
import com.erel.chillsounds.service.entity.Track;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesContract.View {

    List<Track> favorites = new ArrayList<>();
    FavoritesAdapter adapter = new FavoritesAdapter();

    FavoritesContract.Presenter presenter = null;

    RecyclerView favoritesRecycler;
    CircularProgressBar loadingProgress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FavoritesPresenter(getContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoritesRecycler = view.findViewById(R.id.favoritesRecycler);
        favoritesRecycler.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));
        favoritesRecycler.setAdapter(adapter);

        loadingProgress = view.findViewById(R.id.loadingProgress);

        presenter.requestFavorites();
    }

    @Override
    public void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void showFavorites(List<Track> favorites) {
        this.favorites = favorites;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        favoritesRecycler.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgress.setVisibility(View.GONE);
        favoritesRecycler.setVisibility(View.VISIBLE);
    }

    class FavoritesAdapter extends RecyclerView.Adapter<FavoriteViewHolder>{

        @NonNull
        @Override
        public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_favorite, parent, false);
            return new FavoriteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final FavoriteViewHolder holder, int position) {
            Track item = favorites.get(position);
            holder.itemName.setText(item.name);
            holder.volumeSeek.setProgress(Float.valueOf(item.volume * 100).intValue());
            holder.volumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        presenter.setVolumeForIndex(holder.getAdapterPosition(), progress / 100f);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            holder.removeFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.removeFavorite(holder.getAdapterPosition());
                }
            });
            if(item.isPlaying){
                holder.playStop.setImageResource(R.drawable.ic_stop);
            }else{
                holder.playStop.setImageResource(R.drawable.ic_play);
            }
            holder.playStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(presenter.playStopForIndex(holder.getAdapterPosition())){
                        holder.playStop.setImageResource(R.drawable.ic_stop);
                    }else{
                        holder.playStop.setImageResource(R.drawable.ic_play);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return favorites.size();
        }
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        SeekBar volumeSeek;
        ImageButton removeFavorite;
        ImageButton playStop;

        FavoriteViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            volumeSeek = itemView.findViewById(R.id.volumeSeek);
            removeFavorite = itemView.findViewById(R.id.removeFavorite);
            playStop = itemView.findViewById(R.id.playStop);
        }
    }
}
