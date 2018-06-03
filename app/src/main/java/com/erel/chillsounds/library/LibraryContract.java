package com.erel.chillsounds.library;

import com.erel.chillsounds.service.entity.Category;
import com.erel.chillsounds.service.entity.Track;

import java.util.List;

public interface LibraryContract {

    interface View{
        void showLibrary(List<Category> library);
        void showLoading();
        void hideLoading();
        void openCategoryView(Category category);
    }

    interface Presenter{
        void requestLibrary();
        void openCategory(int index);
    }

}
