package com.erel.chillsounds.favorites;

import com.erel.chillsounds.service.entity.Track;

import java.util.ArrayList;
import java.util.List;

public class FavoritesInjection {

    private volatile static FavoritesInjection instance;

    public synchronized static FavoritesInjection getInstance(){
        if(instance == null){
            instance = new FavoritesInjection();
        }
        return instance;
    }

    private List<Track> favorites = new ArrayList<>();
    private List<FavoritesWatcher> watchers = new ArrayList<>();

    private FavoritesInjection(){}

    public interface FavoritesWatcher{
        void onFavoritesChanged(List<Track> favorites);
    }

    private void notifyWatchers(){
        for(FavoritesWatcher watcher:watchers){
            if(watcher != null) {
                watcher.onFavoritesChanged(favorites);
            }
        }
    }

    public void addWatcher(FavoritesWatcher watcher){
        watchers.add(watcher);
    }

    public void removeWatcher(FavoritesWatcher watcher){
        watchers.remove(watcher);
    }

    public void initWithFavorites(List<Track> favorites) {
        this.favorites = favorites;
        notifyWatchers();
    }

    public void addFavorite(Track track){
        favorites.add(track);
        notifyWatchers();
    }

    public void removeFavorite(Track track){
        favorites.remove(track);
        notifyWatchers();
    }

    public void removeFavorite(int index){
        favorites.remove(index);
        notifyWatchers();
    }

    public List<Track> getFavorites() {
        return favorites;
    }
}
