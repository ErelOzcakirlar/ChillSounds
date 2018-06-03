package com.erel.chillsounds.library;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.Nullable;

import com.erel.chillsounds.favorites.FavoritesContract;
import com.erel.chillsounds.favorites.FavoritesInjection;
import com.erel.chillsounds.service.RSWebService;
import com.erel.chillsounds.service.ServiceResponse;
import com.erel.chillsounds.service.entity.Category;
import com.erel.chillsounds.service.entity.Track;

import java.io.IOException;
import java.util.List;

public class LibraryPresenter implements LibraryContract.Presenter {

    private Context context;
    private LibraryContract.View libraryView;

    private List<Category> library;

    public LibraryPresenter(Context context, LibraryContract.View libraryView){
        this.context = context;
        this.libraryView = libraryView;
    }

    @Override
    public void requestLibrary() {
        libraryView.showLoading();
        RSWebService.getInstance(context).getLibrary(new RSWebService.ResponseCallback() {
            @Override
            public void onResponse(boolean success, @Nullable ServiceResponse response) {
                libraryView.hideLoading();
                if(success && response != null && response.library != null){
                    library = response.library;
                    libraryView.showLibrary(response.library);
                }
            }
        });
    }

    @Override
    public void openCategory(int index) {
        libraryView.openCategoryView(library.get(index));
    }
}
