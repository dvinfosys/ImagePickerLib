package com.dvinfosys.imagepicker.ui.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dvinfosys.imagepicker.R;
import com.dvinfosys.imagepicker.model.Config;
import com.dvinfosys.imagepicker.model.Image;
import com.dvinfosys.imagepicker.ui.common.BasePresenter;

import java.util.List;

public class CameraPresenter extends BasePresenter<CameraView> {

    private CameraModule cameraModule = new DefaultCameraModule();

    public CameraPresenter() {
    }


    void captureImage(Activity activity, Config config, int requestCode) {
        Context context = activity.getApplicationContext();
        Intent intent = cameraModule.getCameraIntent(activity, config);
        if (intent == null) {
            Toast.makeText(context, context.getString(R.string.imagepicker_error_create_image_file), Toast.LENGTH_LONG).show();
            return;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public void finishCaptureImage(Context context, Intent data, final Config config) {
        cameraModule.getImage(context, data, new OnImageReadyListener() {
            @Override
            public void onImageReady(List<Image> images) {
                getView().finishPickImages(images);
            }
        });
    }
}
