package com.erel.chillsounds.library;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.erel.chillsounds.R;
import com.erel.chillsounds.category.CategoryFragment;
import com.erel.chillsounds.service.entity.Category;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment implements LibraryContract.View {

    List<Category> library = new ArrayList<>();
    CategoriesAdapter adapter = new CategoriesAdapter();

    LibraryContract.Presenter presenter = null;

    RecyclerView libraryRecycler;
    CircularProgressBar loadingProgress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LibraryPresenter(getContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        libraryRecycler = view.findViewById(R.id.libraryRecycler);
        libraryRecycler.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));
        libraryRecycler.setAdapter(adapter);

        loadingProgress = view.findViewById(R.id.loadingProgress);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.requestLibrary();
    }

    @Override
    public void showLibrary(List<Category> library) {
        this.library = library;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        libraryRecycler.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgress.setVisibility(View.GONE);
        libraryRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void openCategoryView(Category category) {

        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(CategoryFragment.ARG_KEY_CATEGORY, category);
        fragment.setArguments(args);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        String tag = "CATEGORY";
        transaction.replace(R.id.childContainer, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    class CategoriesAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            return new CategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {
            Category item = library.get(position);
            holder.itemName.setText(item.name);
            Glide.with(getContext()).load(item.cover).into(holder.itemCover);
            holder.itemCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.openCategory(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return library.size();
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        RoundedImageView itemCover;

        CategoryViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemCover = itemView.findViewById(R.id.itemCover);
        }
    }
}
