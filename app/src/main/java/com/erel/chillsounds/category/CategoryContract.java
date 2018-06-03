package com.erel.chillsounds.category;

import com.erel.chillsounds.service.entity.Category;
import com.erel.chillsounds.service.entity.Track;

import java.util.List;

public interface CategoryContract {

    interface View{
        Category getCategory();
        void showTracks(List<Track> tracks);
    }

    interface Presenter{
        void requestCategory();
        void favoriteForIndex(int index);
        boolean isTrackFavorite(Track track);
    }

}
