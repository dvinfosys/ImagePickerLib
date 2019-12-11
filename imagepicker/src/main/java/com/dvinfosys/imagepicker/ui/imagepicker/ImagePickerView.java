package com.dvinfosys.imagepicker.ui.imagepicker;

import com.dvinfosys.imagepicker.model.Folder;
import com.dvinfosys.imagepicker.model.Image;
import com.dvinfosys.imagepicker.ui.common.MvpView;

import java.util.List;

public interface ImagePickerView extends MvpView {

    void showLoading(boolean isLoading);

    void showFetchCompleted(List<Image> images, List<Folder> folders);

    void showError(Throwable throwable);

    void showEmpty();

    void showCapturedImage(List<Image> images);

    void finishPickImages(List<Image> images);

}