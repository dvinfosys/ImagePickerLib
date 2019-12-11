package com.dvinfosys.imagepicker.ui.camera;

import android.content.Context;
import android.content.Intent;

import com.dvinfosys.imagepicker.model.Config;

public interface CameraModule {
    Intent getCameraIntent(Context context, Config config);

    void getImage(Context context, Intent intent, OnImageReadyListener imageReadyListener);
}
