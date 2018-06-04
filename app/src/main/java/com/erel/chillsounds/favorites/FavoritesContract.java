package com.erel.chillsounds.favorites;

import com.erel.chillsounds.service.entity.Track;

import java.util.List;

public interface FavoritesContract {

    interface View{
        void showFavorites(List<Track> favorites);
        void showLoading();
        void hideLoading();
    }

    interface Presenter{
        void requestFavorites();
        boolean playStopForIndex(int index);
        void setVolumeForIndex(int index, float volume);
        void removeFavorite(int index);
        void destroy();
    }

}
