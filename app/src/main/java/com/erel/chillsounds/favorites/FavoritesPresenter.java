package com.erel.chillsounds.favorites;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.Nullable;

import com.erel.chillsounds.service.RSWebService;
import com.erel.chillsounds.service.ServiceResponse;
import com.erel.chillsounds.service.entity.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoritesPresenter implements FavoritesContract.Presenter {

    private Context context;
    private SoundPool soundPool;

    private FavoritesContract.View favoritesView;
    private List<Track> favorites = new ArrayList<>();

    public FavoritesPresenter(Context context, FavoritesContract.View favoritesView){
        this.context = context;
        this.favoritesView = favoritesView;
        if(Build.VERSION.SDK_INT < 21){
            this.soundPool = new SoundPool(Integer.MAX_VALUE, AudioManager.STREAM_MUSIC, 100);
        }else{
            this.soundPool = new SoundPool.Builder().setMaxStreams(Integer.MAX_VALUE).build();
        }

    }

    @Override
    public void requestFavorites() {
        RSWebService.getInstance(context).getFavorites(new RSWebService.ResponseCallback() {
            @Override
            public void onResponse(boolean success, @Nullable ServiceResponse response) {
                if(success && response != null && response.favorites != null){
                    FavoritesPresenter.this.favorites = response.favorites;
                    favoritesView.showFavorites(FavoritesPresenter.this.favorites);
                }
            }
        });
    }

    @Override
    public boolean playStopForIndex(int index) {
        Track track = favorites.get(index);
        if(track.isPlaying){
            soundPool.stop(track.poolId);
            track.isPlaying = false;
        }else{
            try {
                int id = soundPool.load(context.getAssets().openFd(track.file), 1);
                soundPool.play(id, track.volume, track.volume, 1, 1, 1f);
                track.isPlaying = true;
            }catch (IOException ignored){}
        }
        return track.isPlaying; // to set button state
    }

    @Override
    public void setVolumeForIndex(int index, float volume) {
        Track track = favorites.get(index);
        track.volume = volume;
        soundPool.setVolume(track.poolId, volume, volume);
    }

    @Override
    public void removeFavorite(int index) {
        favorites.remove(index);
        favoritesView.showFavorites(favorites);
    }

}
