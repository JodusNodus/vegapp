package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitEventListenerAdapter;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import be.tabtabstudio.veganapp.R;

public class ProductCameraActivity extends AppCompatActivity {

    private CreateProductViewModel mViewModel;
    private CameraView cameraView;
    private FloatingActionButton cameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_camera);
        mViewModel = ViewModelProviders.of(this).get(CreateProductViewModel.class);
        mViewModel.setContext(this);

        cameraView = findViewById(R.id.camera);

        cameraView.addCameraKitListener(new CameraKitEventListenerAdapter() {
            @Override
            public void onImage(CameraKitImage image) {
                finish();
                mViewModel.uploadProductImage(image);
            }
        });

        cameraBtn = findViewById(R.id.take_picture_btn);
        cameraBtn.setOnClickListener(view -> {
            disableBtn();
            hideCameraView();
            cameraView.captureImage();
        });
    }

    private void disableBtn() {
        cameraBtn.setEnabled(false);
        cameraBtn.setAlpha(0.5f);
    }

    private void hideCameraView() {
        cameraView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }
}
