package be.tabtabstudio.veganapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import be.tabtabstudio.veganapp.R;

public class ProductCameraActivity extends AppCompatActivity implements CameraKitEventListener {

    private CameraView cameraView;
    private FloatingActionButton cameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_camera);

        cameraView = findViewById(R.id.camera);

        cameraView.addCameraKitListener(this);

        cameraBtn = findViewById(R.id.take_picture_btn);
        cameraBtn.setOnClickListener(view -> {
            disableBtn();
            cameraView.captureImage();
        });
    }

    private void disableBtn() {
        cameraBtn.setEnabled(false);
        cameraBtn.setAlpha(0.5f);
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

    @Override
    public void onEvent(CameraKitEvent cameraKitEvent) {

    }

    @Override
    public void onError(CameraKitError cameraKitError) {

    }

    @Override
    public void onImage(CameraKitImage cameraKitImage) {
        Bitmap res = cameraKitImage.getBitmap();
        finish();
    }

    @Override
    public void onVideo(CameraKitVideo cameraKitVideo) {

    }
}
