package be.tabtabstudio.veganapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wonderkiln.camerakit.CameraKitImage;

import java.util.List;

import be.tabtabstudio.veganapp.R;

public class CreateProductActivity extends AppCompatActivity {

    private CreateProductViewModel mViewModel;
    private ImageView newProductImage;
    private TextView newProductBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        newProductImage = findViewById(R.id.new_product_image);
        newProductBarcode = findViewById(R.id.new_product_barcode);

        mViewModel = ViewModelProviders.of(this).get(CreateProductViewModel.class);
        mViewModel.setContext(this);
        mViewModel.startNewForm();

        mViewModel.getEanObservable().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long ean) {
                mViewModel.doesProductAlreadyExist(ean);
                setBarcode(ean);
            }
        });

        mViewModel.getAlreadyExistsObservable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean alreadyExists) {
                if (alreadyExists) {
                    finish();
                    Toast.makeText(getApplicationContext(), R.string.product_already_exists, Toast.LENGTH_SHORT);
                } else {
                    takePicture();
                }
            }
        });

        mViewModel.getLabelSuggestionsObservable().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> labelSuggestions) {

            }
        });

        scanBarcode();
    }

    private void setBarcode(Long ean) {
        newProductBarcode.setText(ean.toString());
    }

    private void setImage(Bitmap bitmap) {
        newProductImage.setImageBitmap(bitmap);
    }

    private void scanBarcode() {
        Intent k = new Intent(this, BarcodeScannerActivity.class);
        startActivity(k);
    }

    private void takePicture() {
        Intent k = new Intent(this, ProductCameraActivity.class);
        startActivity(k);
    }
}
