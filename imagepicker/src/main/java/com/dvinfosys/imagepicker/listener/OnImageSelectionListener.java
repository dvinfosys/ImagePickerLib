package com.dvinfosys.imagepicker.listener;

import com.dvinfosys.imagepicker.model.Image;

import java.util.List;

public interface OnImageSelectionListener {
    void onSelectionUpdate(List<Image> images);
}
