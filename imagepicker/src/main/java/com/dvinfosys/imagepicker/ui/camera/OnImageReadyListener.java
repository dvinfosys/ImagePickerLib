package com.dvinfosys.imagepicker.ui.camera;


import com.dvinfosys.imagepicker.model.Image;

import java.util.List;

public interface OnImageReadyListener {
    void onImageReady(List<Image> images);
}
