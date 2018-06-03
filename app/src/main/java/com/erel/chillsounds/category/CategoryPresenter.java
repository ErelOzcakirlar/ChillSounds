package com.erel.chillsounds.category;

import android.content.Context;
import android.support.annotation.Nullable;

import com.erel.chillsounds.favorites.FavoritesInjection;
import com.erel.chillsounds.service.RSWebService;
import com.erel.chillsounds.service.ServiceResponse;
import com.erel.chillsounds.service.entity.Category;
import com.erel.chillsounds.service.entity.Track;

import java.util.List;

public class CategoryPresenter implements CategoryContract.Presenter, FavoritesInjection.FavoritesWatcher {

    private CategoryContract.View categoryView;

    private Category category;

    public CategoryPresenter(CategoryContract.View categoryView){
        this.categoryView = categoryView;
        this.category = categoryView.getCategory();
        FavoritesInjection.getInstance().addWatcher(this);
    }

    @Override
    public void requestCategory() {
        categoryView.showTracks(category.tracks);
    }

    @Override
    public void favoriteForIndex(int index) {
        Track track = category.tracks.get(index);
        if(isTrackFavorite(track)){
            FavoritesInjection.getInstance().removeFavorite(track);
        }else{
            FavoritesInjection.getInstance().addFavorite(track);
        }
    }

    @Override
    public boolean isTrackFavorite(Track track) {
        return FavoritesInjection.getInstance().getFavorites().contains(track);
    }

    @Override
    public void onFavoritesChanged(List<Track> favorites) {
        categoryView.showTracks(category.tracks);
    }
}
