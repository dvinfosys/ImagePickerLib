package com.dvinfosys.imagepicker.ui.imagepicker;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.dvinfosys.imagepicker.R;
import com.dvinfosys.imagepicker.adapter.FolderPickerAdapter;
import com.dvinfosys.imagepicker.adapter.ImagePickerAdapter;
import com.dvinfosys.imagepicker.listener.OnBackAction;
import com.dvinfosys.imagepicker.listener.OnFolderClickListener;
import com.dvinfosys.imagepicker.listener.OnImageClickListener;
import com.dvinfosys.imagepicker.listener.OnImageSelectionListener;
import com.dvinfosys.imagepicker.model.Config;
import com.dvinfosys.imagepicker.model.Folder;
import com.dvinfosys.imagepicker.model.Image;
import com.dvinfosys.imagepicker.widget.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewManager {

    private Context context;
    private RecyclerView recyclerView;
    private Config config;

    private GridLayoutManager layoutManager;
    private GridSpacingItemDecoration itemOffsetDecoration;

    private ImagePickerAdapter imageAdapter;
    private FolderPickerAdapter folderAdapter;

    private int imageColumns;
    private int folderColumns;

    private ImageLoader imageLoader;

    private Parcelable foldersState;
    private String title;
    private boolean isShowingFolder;


    public RecyclerViewManager(RecyclerView recyclerView, Config config, int orientation) {
        this.recyclerView = recyclerView;
        this.config = config;
        context = recyclerView.getContext();
        changeOrientation(orientation);
        imageLoader = new ImageLoader();
        isShowingFolder = config.isFolderMode();
    }

    public void setupAdapters(OnImageClickListener imageClickListener, final OnFolderClickListener folderClickListener) {
        ArrayList<Image> selectedImages = null;
        if (config.isMultipleMode() && !config.getSelectedImages().isEmpty()) {
            selectedImages = config.getSelectedImages();
        }

        imageAdapter = new ImagePickerAdapter(context, imageLoader, selectedImages, imageClickListener);
        folderAdapter = new FolderPickerAdapter(context, imageLoader, new OnFolderClickListener() {
            @Override
            public void onFolderClick(Folder folder) {
                foldersState = recyclerView.getLayoutManager().onSaveInstanceState();
                folderClickListener.onFolderClick(folder);
            }
        });
    }

    /**
     * Set item size, column size base on the screen orientation
     */
    public void changeOrientation(int orientation) {
        imageColumns = orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5;
        folderColumns = orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4;

        int columns = isShowingFolder ? folderColumns : imageColumns;
        layoutManager = new GridLayoutManager(context, columns);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setItemDecoration(columns);
    }

    private void setItemDecoration(int columns) {
        if (itemOffsetDecoration != null) {
            recyclerView.removeItemDecoration(itemOffsetDecoration);
        }
        itemOffsetDecoration = new GridSpacingItemDecoration(columns,
                context.getResources().getDimensionPixelSize(R.dimen.imagepicker_item_padding),
                false
        );
        recyclerView.addItemDecoration(itemOffsetDecoration);
        layoutManager.setSpanCount(columns);
    }


    public void setOnImageSelectionListener(OnImageSelectionListener imageSelectionListener) {
        checkAdapterIsInitialized();
        imageAdapter.setOnImageSelectionListener(imageSelectionListener);
    }

    public List<Image> getSelectedImages() {
        checkAdapterIsInitialized();
        return imageAdapter.getSelectedImages();
    }

    public void addSelectedImages(List<Image> images) {
        imageAdapter.addSelected(images);
    }

    private void checkAdapterIsInitialized() {
        if (imageAdapter == null) {
            throw new IllegalStateException("Must call setupAdapters first!");
        }
    }

    public boolean selectImage() {
        if (config.isMultipleMode()) {
            if (imageAdapter.getSelectedImages().size() >= config.getMaxSize()) {
                String message = String.format(config.getLimitMessage(), config.getMaxSize());
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (imageAdapter.getItemCount() > 0) {
                imageAdapter.removeAllSelected();
            }
        }
        return true;
    }

    public void handleBack(OnBackAction action) {
        if (config.isFolderMode() && !isShowingFolder) {
            setFolderAdapter(null);
            action.onBackToFolder();
            return;
        }
        action.onFinishImagePicker();
    }

    public void setImageAdapter(List<Image> images, String title) {
        imageAdapter.setData(images);
        setItemDecoration(imageColumns);
        recyclerView.setAdapter(imageAdapter);
        this.title = title;
        isShowingFolder = false;
    }

    public void setFolderAdapter(List<Folder> folders) {
        folderAdapter.setData(folders);
        setItemDecoration(folderColumns);
        recyclerView.setAdapter(folderAdapter);
        isShowingFolder = true;

        if (foldersState != null) {
            layoutManager.setSpanCount(folderColumns);
            recyclerView.getLayoutManager().onRestoreInstanceState(foldersState);
        }
    }

    public String getTitle() {
        if (isShowingFolder) {
            return config.getFolderTitle();
        } else if (config.isFolderMode()) {
            return title;
        } else {
            return config.getImageTitle();
        }
    }

    public boolean isShowDoneButton() {
        return config.isMultipleMode() && (config.isAlwaysShowDoneButton() || imageAdapter.getSelectedImages().size() > 0);
    }
}
