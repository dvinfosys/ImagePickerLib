package com.dvinfosys.imagepicker.ui.camera;

import com.dvinfosys.imagepicker.model.Image;
import com.dvinfosys.imagepicker.ui.common.MvpView;

import java.util.List;

public interface CameraView extends MvpView {

    void finishPickImages(List<Image> images);
}
