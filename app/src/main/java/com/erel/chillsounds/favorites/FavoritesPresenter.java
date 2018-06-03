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
import java.util.List;

public class FavoritesPresenter implements FavoritesContract.Presenter, FavoritesInjection.FavoritesWatcher {

    private Context context;
    private SoundPool soundPool;

    private FavoritesContract.View favoritesView;

    public FavoritesPresenter(Context context, FavoritesContract.View favoritesView){
        this.context = context;
        this.favoritesView = favoritesView;
        if(Build.VERSION.SDK_INT < 21){
            this.soundPool = new SoundPool(Integer.MAX_VALUE, AudioManager.STREAM_MUSIC, 0);
        }else{
            this.soundPool = new SoundPool.Builder().setMaxStreams(Integer.MAX_VALUE).build();
        }
        FavoritesInjection.getInstance().addWatcher(this);
    }

    @Override
    public void requestFavorites() {
        favoritesView.showLoading();
        RSWebService.getInstance(context).getFavorites(new RSWebService.ResponseCallback() {
            @Override
            public void onResponse(boolean success, @Nullable ServiceResponse response) {
                favoritesView.hideLoading();
                if(success && response != null && response.favorites != null){
                    FavoritesInjection.getInstance().initWithFavorites(response.favorites);
                }
            }
        });
    }

    @Override
    public boolean playStopForIndex(int index) {
        final Track track =  FavoritesInjection.getInstance().getFavorites().get(index);
        if(track.isPlaying){
            soundPool.stop(track.poolId);
            track.isPlaying = false;
        }else{
            try {
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        soundPool.play(track.poolId, track.volume, track.volume, 1, -1, 1f);
                    }
                });
                track.poolId = soundPool.load(context.getAssets().openFd(track.file), 1);
                track.isPlaying = true;
            }catch (IOException ignored){}
        }
        return track.isPlaying; // to set button state
    }

    @Override
    public void setVolumeForIndex(int index, float volume) {
        Track track = FavoritesInjection.getInstance().getFavorites().get(index);
        track.volume = volume;
        soundPool.setVolume(track.poolId, volume, volume);
    }

    @Override
    public void removeFavorite(int index) {
        FavoritesInjection injection = FavoritesInjection.getInstance();
        Track track = injection.getFavorites().get(index);
        if(track.isPlaying){
            soundPool.stop(track.poolId);
        }
        injection.removeFavorite(index);
    }

    @Override
    public void onFavoritesChanged(List<Track> favorites) {
        favoritesView.showFavorites(favorites);
    }
}
