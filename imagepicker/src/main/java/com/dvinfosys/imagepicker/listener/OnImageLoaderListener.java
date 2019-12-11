package com.dvinfosys.imagepicker.listener;

import com.dvinfosys.imagepicker.model.Folder;
import com.dvinfosys.imagepicker.model.Image;

import java.util.List;

public interface OnImageLoaderListener {
    void onImageLoaded(List<Image> images, List<Folder> folders);

    void onFailed(Throwable throwable);
}
